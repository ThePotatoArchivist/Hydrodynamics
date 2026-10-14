package archives.tater.hydrodynamics.mixin;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
		return original || getVehicle() != null && getVehicle().is(HydrodynamicsModEntityTags.SUBMERGES_PASSENGERS);
	}

	@ModifyReturnValue(
			method = "isEyeInFluid",
			at = @At("RETURN")
	)
	private boolean submergePassenger(boolean original, TagKey<Fluid> type) {
		return original || type == FluidTags.WATER && getVehicle() != null && getVehicle().is(HydrodynamicsModEntityTags.SUBMERGES_PASSENGERS);
	}
}