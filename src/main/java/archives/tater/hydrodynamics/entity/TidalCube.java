package archives.tater.hydrodynamics.entity;

import archives.tater.hydrodynamics.registry.*;

import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;

import static java.lang.Math.max;
import static java.lang.Math.sqrt;
import static net.minecraft.util.Mth.*;

public class TidalCube extends AbstractCubeMob implements Bucketable {

    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TidalCube.class, EntityDataSerializers.BOOLEAN);;

    public TidalCube(EntityType<? extends TidalCube> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, 0f);
        moveControl = new SwimmingCubeMoveControl(this);
        navigation = new AmphibiousPathNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        var holdableLeashed = Leashable.leashableInArea(level(), position(), l -> l.getLeashHolder() == player && canHold((Entity) l));
        if (!holdableLeashed.isEmpty()) {
            if (isVehicle()) return InteractionResult.FAIL;

            if (level().isClientSide()) return InteractionResult.SUCCESS;

            var entity = (Entity & Leashable) holdableLeashed.getFirst();
            entity.dropLeash();
            entity.startRiding(this);

            return InteractionResult.SUCCESS;
        }

        var stack = player.getItemInHand(hand);
        var food = stack.get(HydrodynamicsModComponents.TIDAL_CUBE_FOOD);
        if (food != null) {
            if (!(level() instanceof ServerLevel serverLevel)) return InteractionResult.CONSUME;

            var context = new LootContext.Builder(new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.TARGET_ENTITY, this)
                    .withOptionalParameter(LootContextParams.INTERACTING_ENTITY, player)
                    .withParameter(LootContextParams.TOOL, stack)
                    .create(LootContextParamSets.ENTITY_INTERACT)
            ).create(Optional.empty());

            if (!serverLevel.getServer().reloadableRegistries().lookup().getOrThrow(HydrodynamicsModPredicates.CAN_FEED_TIDAL_CUBE).value().test(context))
                return InteractionResult.FAIL;

            setSize(getSize() + food.sizes().get(context, 0), true);
            if (food.consume())
                stack.consume(1, player);
            // TODO sound
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY(0.5), getZ(), 8, getBbWidth() / 2, getBbHeight() / 2, getBbWidth() / 2, 0.0);

            return InteractionResult.SUCCESS_SERVER;
        }

        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void tick() {
        super.tick();

        collectPassenger();

        if (!level().isClientSide() || onGround()) return;

        if (random.nextInt(2 * max(1, 6 - getSize())) == 0) // bigger slimes drip more because they have a wider base
            level().addParticle(ParticleTypes.FALLING_WATER, getRandomX(getBbWidth() / 4), getY(), getRandomZ(getBbWidth() / 4), 0, 0, 0);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (level() instanceof ServerLevel serverLevel)
            if (serverLevel.environmentAttributes().getValue(HydrodynamicsModEnvironment.TIDAL_CUBE_EVAPORATES, position()))
                hurtServer(serverLevel, damageSources().dryOut(), 1.0F);
    }

    private void collectPassenger() {
        if (level().isClientSide() || isVehicle()) return;

        var entities = level().getEntities(this, getBoundingBox().inflate(0.2f, 0f, 0.2f), EntitySelector.pushableBy(this));
        if (entities.isEmpty()) return;

        for (var entity : entities)
            if (!entity.isPassenger() && canHold(entity) && entity.startRiding(this))
                break;
    }

    private boolean canHold(Entity entity) {
        return entity instanceof LivingEntity
                && !(entity instanceof TidalCube)
                && entity.is(HydrodynamicsModEntityTags.TIDAL_CUBE_CAN_HOLD)
                && getRequiredSize(entity) < getBbWidth();
    }

    private static float getRequiredSize(Entity entity) {
        var size = max(entity.getBbWidth(), entity.getBbHeight());
        return entity.is(HydrodynamicsModEntityTags.REQUIRES_LARGER_TIDAL_CUBE) ? size + 0.5f : size;
    }

    @Override
    public void die(DamageSource source) {
        ejectPassengers();
        super.die(source);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new TidalCubeTemptGoal(this, 1, stack -> stack.has(HydrodynamicsModComponents.TIDAL_CUBE_FOOD), false, 1));
        goalSelector.addGoal(3, new CubeMobSwimmingGoal(this, 1, 40));
        goalSelector.addGoal(4, new CubeMobRandomDirectionGoal(this));
        goalSelector.addGoal(5, new CubeMobKeepOnJumpingGoal(this));
    }

    @Override
    protected void addBehaviourGoals() {
    }

    @Override
    protected void addTargetingGoals() {
    }

    @Override
    protected boolean canDealDamage() {
        return false;
    }

    protected void setSpawnSize(final ServerLevelAccessor level, final DifficultyInstance difficulty) {
        setSize(level.getRandom().nextInt(4), true);
    }

    @Override
    protected int getSplitCount() {
        return 2;
    }

    @Override
    protected @Nullable ParticleOptions getParticleType() {
        return ParticleTypes.SPLASH;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
        return new Vec3(0.0, (dimensions.height() - passenger.getBbHeight()) / 2.0 + passenger.getVehicleAttachmentPoint(this).y, 0.0);
    }

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        return null; // TODO player riding
    }

    @Override
    public boolean supportQuadLeash() {
        return true;
    }

    @Override
    public Vec3[] getQuadLeashOffsets() {
        return Leashable.createQuadLeashOffsets(this, 0.0, 0.5, 0.5, 1);
    }

    @Override
    protected boolean isImmobile() { // Allows leash to apply angular momentum, normally CubeMobMoveControl overrides it
        return super.isImmobile() || getLeashHolder() != null && getLeashHolder().supportQuadLeashAsHolder();
    }

    @Override
    public boolean checkSpawnObstruction(final LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    // TODO unique sounds

    @Override
    protected SoundEvent getJumpSound() {
        return SoundEvents.SLIME_JUMP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    @Override
    protected SoundEvent getSquishSound() {
        return SoundEvents.SLIME_SQUISH;
    }

    public static AttributeSupplier.Builder createTidalCubeAttributes() {
        return Mob.createMobAttributes().add(Attributes.TEMPT_RANGE, 8);
    }

    @Override
    public boolean fromBucket() {
        return entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        entityData.set(FROM_BUCKET, fromBucket);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        bucket.copyFrom(HydrodynamicsModComponents.TIDAL_CUBE_SIZE, this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    public <T> @Nullable T get(final DataComponentType<? extends T> type) {
        return type == HydrodynamicsModComponents.TIDAL_CUBE_SIZE ? castComponentValue(type, getSize()) : super.get(type);
    }

    protected void applyImplicitComponents(final DataComponentGetter components) {
        applyImplicitComponentIfPresent(components, HydrodynamicsModComponents.TIDAL_CUBE_SIZE);
        super.applyImplicitComponents(components);
    }

    protected <T> boolean applyImplicitComponent(final DataComponentType<T> type, final T value) {
        if (type == HydrodynamicsModComponents.TIDAL_CUBE_SIZE) {
            setSize(castComponentValue(HydrodynamicsModComponents.TIDAL_CUBE_SIZE, value), true);
            return true;
        }

        return super.applyImplicitComponent(type, value);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return HydrodynamicsModItems.TIDAL_CUBE_BUCKET.getDefaultInstance();
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_SULFUR_CUBE; // TODO unique sound event
    }

    @Override
    public boolean canBePickedUpWithBucket(ItemStack itemStack) {
        return getSize() <= 3 && (itemStack.is(Items.BUCKET) || Bucketable.super.canBePickedUpWithBucket(itemStack));
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFromBucket(input.getBooleanOr("FromBucket", false));
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(final double distSqr) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    /// @see net.minecraft.world.entity.monster.cubemob.SulfurCube.SulfurCubeTemptGoal
    @SuppressWarnings("JavadocReference")
    private static class TidalCubeTemptGoal extends TemptGoal.ForNonPathfinders {
        public TidalCubeTemptGoal(final Mob mob, final double speedModifier, final Predicate<ItemStack> items, final boolean canScare, final double stopDistance) {
            super(mob, speedModifier, items, canScare, stopDistance);
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        protected void stopNavigation() {
            if (mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl<?> cubeMobMoveControl)
                cubeMobMoveControl.setWantedMovement(0);
        }

        protected void navigateTowards(final Player player) {
            this.mob.lookAt(player, 10.0F, 10.0F);
            if (mob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl<?> cubeMobMoveControl)
                cubeMobMoveControl.setDirection(mob.getYRot(), true);
        }
    }

    private static class CubeMobSwimmingGoal extends RandomSwimmingGoal {

        public CubeMobSwimmingGoal(PathfinderMob mob, double speedModifier, int interval) {
            super(mob, speedModifier, interval);
        }

        @Override
        public boolean canUse() {
            return mob.isInWater() && super.canUse();
        }
    }

    /// @see net.minecraft.world.entity.animal.fish.AbstractFish.FishMoveControl
    @SuppressWarnings("JavadocReference")
    private static class SwimmingCubeMoveControl extends AbstractCubeMob.CubeMobMoveControl<TidalCube> {
        public SwimmingCubeMoveControl(TidalCube mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (!mob.isInWater()) {
                super.tick();
                return;
            }

            if (mob.isEyeInFluid(FluidTags.WATER)) {
                mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.005, 0));
            }

            if (this.operation != Operation.MOVE_TO || mob.getNavigation().isDone()) {
                mob.setSpeed(0f);
                return;
            }
            float targetSpeed = (float) (this.speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            mob.setSpeed(lerp(0.125f, mob.getSpeed(), targetSpeed));
            double xd = this.wantedX - mob.getX();
            double yd = this.wantedY - mob.getY();
            double zd = this.wantedZ - mob.getZ();
            if (yd != 0) {
                double dd = sqrt(xd * xd + yd * yd + zd * zd);
                mob.setDeltaMovement(mob.getDeltaMovement().add(0, mob.getSpeed() * (yd / dd) * 0.1, 0));
            }

            if (xd != 0 || zd != 0) {
                float yRotD = (float)(atan2(zd, xd) * RAD_TO_DEG) - 90f;
                mob.setYRot(this.rotlerp(mob.getYRot(), yRotD, 90f));
                mob.yBodyRot = mob.getYRot();
            }

        }
    }
}
