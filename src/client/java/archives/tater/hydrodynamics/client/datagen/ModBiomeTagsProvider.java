package archives.tater.hydrodynamics.client.datagen;

import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModBiomeTags;
import archives.tater.hydrodynamics.registry.worldgen.HydrodynamicsModBiomes;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagsProvider extends FabricTagsProvider<Biome> {

    public ModBiomeTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, Registries.BIOME, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(HydrodynamicsModBiomeTags.ALLOWS_FISH_SPAWNS_SHALLOW).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(BiomeTags.IS_BEACH).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(BiomeTags.IS_OVERWORLD).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(BiomeTags.HAS_TRIAL_CHAMBERS).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(ConventionalBiomeTags.IS_WET_OVERWORLD).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(ConventionalBiomeTags.IS_SANDY).add(HydrodynamicsModBiomes.TIDE_POOLS);
        builder(ConventionalBiomeTags.IS_TEMPERATE_OVERWORLD).add(HydrodynamicsModBiomes.TIDE_POOLS);
    }
}
