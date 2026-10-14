package archives.tater.hydrodynamics.mixin;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @WrapOperation(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectUtil;hasWaterBreathing(Lnet/minecraft/world/entity/LivingEntity;)Z")
    )
    private boolean noDrown(LivingEntity mob, Operation<Boolean> original) {
        return original.call(mob) || mob.is(HydrodynamicsModEntityTags.WATER_BREATHING_SUBMERGED_PASSENGER) && mob.getVehicle() != null && mob.getVehicle().is(HydrodynamicsModEntityTags.SUBMERGES_PASSENGERS);
    }
}
