package archives.tater.hydrodynamics.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.level.Level;

import org.jspecify.annotations.Nullable;

public class TidalCube extends AbstractCubeMob {
    public TidalCube(EntityType<? extends TidalCube> type, Level level) {
        super(type, level);
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
}
