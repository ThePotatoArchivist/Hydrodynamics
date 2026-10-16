package archives.tater.hydrodynamics.registry.worldgen;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.registry.HydrodynamicsModEntities;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import static net.minecraft.data.worldgen.BiomeDefaultFeatures.*;
import static net.minecraft.data.worldgen.biome.OverworldBiomes.baseBiome;
import static net.minecraft.data.worldgen.biome.OverworldBiomes.globalOverworldGeneration;

public interface HydrodynamicsModBiomes {
    private static ResourceKey<Biome> create(String path) {
        return ResourceKey.create(Registries.BIOME, Hydrodynamics.id(path));
    }

    ResourceKey<Biome> TIDE_POOLS = create("tide_pools");

    static void bootstrap(BootstrapContext<Biome> registry) {
        var placedFeatures = registry.lookup(Registries.PLACED_FEATURE);
        var carvers = registry.lookup(Registries.CARVER);

        registry.register(TIDE_POOLS, tidePools(placedFeatures, carvers));
    }

    private static Biome tidePools(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<WorldCarver> carvers) {
        var mobs = new MobSpawnSettings.Builder();
        mobs.addSpawn(EntityTypes.TURTLE, 5, 2, 5);
        mobs.addSpawn(EntityTypes.TROPICAL_FISH, 25, 8, 8);
        mobs.addSpawn(HydrodynamicsModEntities.TIDAL_CUBE, 100, 2, 4);
        mobs.addSpawn(EntityTypes.BAT, 10, 8, 8);
        mobs.addSpawn(EntityTypes.CREEPER, 50, 2, 2);
        mobs.addSpawn(EntityTypes.SKELETON, 50, 2, 2);
        mobs.addSpawn(EntityTypes.SLIME, 25, 1, 1);
        mobs.addSpawn(EntityTypes.CAVE_SPIDER, 20, 1, 1);
        mobs.addSpawn(EntityTypes.ZOMBIE, 50, 2, 2);
        mobs.addSpawn(EntityTypes.ENDERMAN, 10, 1, 1);
        mobs.addSpawn(EntityTypes.WITCH, 1, 1, 1);
        mobs.addSpawn(EntityTypes.ZOMBIE_VILLAGER, 5, 1, 1);

        var generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);
        globalOverworldGeneration(generation);
        addDefaultOres(generation);
        addDefaultSoftDisks(generation);
        addDefaultFlowers(generation);
        addDefaultGrass(generation);
        addDefaultMushrooms(generation);
        addDefaultExtraVegetation(generation, true);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, HydrodynamicsModPlacements.TIDE_POOL);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, HydrodynamicsModPlacements.TIDE_POOL_AIR_VEGETATION);

        return baseBiome(0.8F, 0.4F)
                .specialEffects(new BiomeSpecialEffects.Builder().waterColor(4159204).build())
                .mobSpawnSettings(mobs.build())
                .generationSettings(generation.build())
                .build();
    }
}
