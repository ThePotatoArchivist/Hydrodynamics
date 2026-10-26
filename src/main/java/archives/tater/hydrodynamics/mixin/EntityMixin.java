package archives.tater.hydrodynamics.mixin;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.Fluid;

import org.jspecify.annotations.Nullable;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public abstract @Nullable Entity getVehicle();

	@ModifyReturnValue(
			method = {
					"isInWater",
					"isUnderWater"
			},
			at = @At("RETURN")
	)
	private boolean submergePassenger(boolean original) {
		return original || vehicleSubmerges();
	}

	@ModifyReturnValue(
			method = "isEyeInFluid",
			at = @At("RETURN")
	)
	private boolean submergePassenger(boolean original, TagKey<Fluid> type) {
		return original || type == FluidTags.WATER && vehicleSubmerges();
	}

	@ModifyExpressionValue(
			method = "applyEffectsFromBlocks(Ljava/util/List;)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isInRain()Z")
	)
	private boolean extinguish(boolean original) {
		return original || vehicleSubmerges();
	}

	@Unique
    private boolean vehicleSubmerges() {
		return getVehicle() != null && getVehicle().is(HydrodynamicsModEntityTags.SUBMERGES_PASSENGERS);
	}
}