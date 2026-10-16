package archives.tater.hydrodynamics.client.mixin;

import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModBiomeTags;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.fish.TropicalFish;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;

@Mixin(WaterAnimal.class)
public class WaterAnimalMixin {
    @ModifyExpressionValue(
            method = "checkSurfaceWaterAnimalSpawnRules",
            at = {
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"),
                    @At(value = "INVOKE", target = "Lnet/minecraft/world/level/material/FluidState;is(Lnet/minecraft/tags/TagKey;)Z")
            }
    )
    private static boolean allowShallow(boolean original, final EntityType<TropicalFish> type, final LevelAccessor level, final EntitySpawnReason spawnReason, final BlockPos pos) {
        return original || level.getBiome(pos).is(HydrodynamicsModBiomeTags.ALLOWS_FISH_SPAWNS_SHALLOW);
    }
}
