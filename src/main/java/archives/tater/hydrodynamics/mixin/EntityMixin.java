package archives.tater.hydrodynamics.mixin;

import archives.tater.hydrodynamics.entity.TidalCube;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.world.entity.Entity;

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
	private boolean tidalCubeIsWater(boolean original) {
		return original || getVehicle() instanceof TidalCube;
	}
}