package archives.tater.hydrodynamics.entity;

import archives.tater.hydrodynamics.registry.HydrodyamicsModPredicates;
import archives.tater.hydrodynamics.registry.HydrodynamicsModComponents;
import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import java.util.Optional;

import static java.lang.Math.*;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static net.minecraft.util.Mth.*;
import static net.minecraft.util.Mth.atan2;
import static net.minecraft.util.Mth.clamp;
import static net.minecraft.util.Mth.cos;
import static net.minecraft.util.Mth.sin;

public class TidalCube extends AbstractCubeMob {
    public TidalCube(EntityType<? extends TidalCube> type, Level level) {
        super(type, level);
        setPathfindingMalus(PathType.WATER, 0.0F);
        moveControl = new TidalCubeMoveControl(this, 5, 15, 1F, true);
        navigation = new AmphibiousPathNavigation(this, level);
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

            if (!serverLevel.getServer().reloadableRegistries().lookup().getOrThrow(HydrodyamicsModPredicates.CAN_FEED_TIDAL_CUBE).value().test(context))
                return InteractionResult.FAIL;

            setSize(getSize() + food.sizes().get(context, 0), true);
            if (food.consume())
                stack.consume(1, player);
            // TODO sound
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER, getX(), getY(0.5), getZ(), 8, getBbWidth() / 2, getBbHeight() / 2, getBbWidth() / 2, 0.0);

            return InteractionResult.SUCCESS_SERVER;
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tick() {
        super.tick();

        collectPassenger();

        if (!level().isClientSide() || onGround()) return;

        if (random.nextInt(2 * max(1, 6 - getSize())) == 0) // bigger slimes drip more because they have a wider base
            level().addParticle(ParticleTypes.FALLING_WATER, getRandomX(getBbWidth() / 4), getY(), getRandomZ(getBbWidth() / 4), 0, 0, 0);
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
    protected void registerGoals() {
        goalSelector.addGoal(1, new CubeMobSwimmingGoal(this, 1, 40));
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

    public static AttributeSupplier.Builder createSulfurCubeAttributes() {
        return Mob.createMobAttributes();
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

    /// @see net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl
    private static class TidalCubeMoveControl extends AbstractCubeMob.CubeMobMoveControl<TidalCube> {
        private final int maxTurnX;
        private final int maxTurnY;
        private final float inWaterSpeedModifier;
        private final boolean applyGravity;

        public TidalCubeMoveControl(TidalCube mob, int maxTurnX, int maxTurnY, float inWaterSpeedModifier, boolean applyGravity) {
            super(mob);
            this.maxTurnX = maxTurnX;
            this.maxTurnY = maxTurnY;
            this.inWaterSpeedModifier = inWaterSpeedModifier;
            this.applyGravity = applyGravity;
        }

        @Override
        public void tick() {
            if (!mob.isInWater()) {
                super.tick();
                return;
            }

            if (applyGravity && mob.isEyeInFluid(FluidTags.WATER)) {
                mob.setDeltaMovement(mob.getDeltaMovement().add(0.0F, 0.005, 0.0F));
            }

            if (operation != Operation.MOVE_TO || mob.getNavigation().isDone()) {
                mob.setSpeed(0.0F);
                mob.setXxa(0.0F);
                mob.setYya(0.0F);
                mob.setZza(0.0F);
                return;
            }

            double xd = wantedX - mob.getX();
            double yd = wantedY - mob.getY();
            double zd = wantedZ - mob.getZ();
            double dd = xd * xd + yd * yd + zd * zd;
            if (dd < EPSILON) {
                mob.setZza(0.0F);
                return;
            }
            float yRotD = (float)(atan2(zd, xd) * RAD_TO_DEG) - 90.0F;
            mob.setYRot(rotlerp(mob.getYRot(), yRotD, maxTurnY));
            mob.yBodyRot = mob.getYRot();
            mob.yHeadRot = mob.getYRot();
            float speed = (float)(speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
            mob.setSpeed(speed * inWaterSpeedModifier);
            double sqrt = sqrt(xd * xd + zd * zd);
            if (abs(yd) > EPSILON || abs(sqrt) > EPSILON) {
                float xRotD = -((float)(atan2(yd, sqrt) * RAD_TO_DEG));
                xRotD = clamp(wrapDegrees(xRotD), -maxTurnX, maxTurnX);
                mob.setXRot(rotateTowards(mob.getXRot(), xRotD, 5.0F));
            }

            float cos = cos(mob.getXRot() * DEG_TO_RAD);
            float sin = sin(mob.getXRot() * DEG_TO_RAD);
            mob.zza = cos * speed;
            mob.yya = -sin * speed;
        }
    }
}
