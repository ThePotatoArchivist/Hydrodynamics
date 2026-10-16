package archives.tater.hydrodynamics.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.data.worldgen.material.OverworldMaterialRules;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;

@Mixin(OverworldMaterialRules.class)
public interface OverworldMaterialRulesAccessor {
    @Accessor
    static ResourceKey<MaterialRule> getBIOME_SURFACE() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    static ResourceKey<MaterialRule> getSAND_OR_SANDSTONE_IF_CEILING() {
        throw new UnsupportedOperationException();
    }
}
