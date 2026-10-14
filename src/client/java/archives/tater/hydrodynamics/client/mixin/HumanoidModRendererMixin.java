package archives.tater.hydrodynamics.client.mixin;

import archives.tater.hydrodynamics.registry.HydrodynamicsModEntityTags;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.world.entity.LivingEntity;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidModRendererMixin {
	@WrapOperation(
			method = "extractHumanoidRenderState",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isPassenger()Z")
	)
	private static boolean nonSittingMount(LivingEntity instance, Operation<Boolean> original) {
		return original.call(instance) && (instance.getVehicle() == null || !instance.getVehicle().is(HydrodynamicsModEntityTags.NON_SITTING_MOUNT));
	}
}