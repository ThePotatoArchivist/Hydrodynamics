package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.client.mixin.OverworldMaterialRulesAccessor;
import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModBiomes;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.material.MaterialRules;
import net.minecraft.world.level.levelgen.material.condition.BiomeCondition;
import net.minecraft.world.level.levelgen.material.rule.MaterialRule;

import dev.worldgen.lithostitched.api.registry.LithostitchedRegistries;
import dev.worldgen.lithostitched.api.worldgen.biomeinjector.BiomeInjector;
import dev.worldgen.lithostitched.api.worldgen.biomeinjector.ParameterBuilder;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;

import java.util.concurrent.CompletableFuture;

public class ModLithostitchedProvider extends FabricDynamicRegistryProvider {
    public ModLithostitchedProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        entries.add(ResourceKey.create(LithostitchedRegistries.BIOME_INJECTOR, Hydrodynamics.id("tide_pools")), BiomeInjector.builder(Level.OVERWORLD)
                .replacePartially(registries.getOrThrow(Biomes.BEACH), registries.getOrThrow(HydrodynamicsModBiomes.TIDE_POOLS), ParameterBuilder.create()
                        .climateMin(BiomeInjector.ClimateParameter.WEIRDNESS, 0.25f)
                        .climateRange(BiomeInjector.ClimateParameter.CONTINENTALNESS, -0.16f, -0.1f)
        ));

        entries.add(ResourceKey.create(LithostitchedRegistries.WORLDGEN_MODIFIER, Hydrodynamics.id("surface_rule/tide_pools")), WorldgenModifier.builder()
                .setMaterialRule(
                        registries.getOrThrow(OverworldMaterialRulesAccessor.getBIOME_SURFACE()),
                        Holder.direct(MaterialRules.ifTrue(
                                new BiomeCondition(HolderSet.direct(registries.getOrThrow(HydrodynamicsModBiomes.TIDE_POOLS))),
                                new MaterialRule.HolderHolder(registries.getOrThrow(OverworldMaterialRulesAccessor.getSAND_OR_SANDSTONE_IF_CEILING()))
                        ))
                )
        );
    }

    @Override
    public String getName() {
        return "Lithostitched";
    }

}
