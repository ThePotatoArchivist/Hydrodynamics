package archives.tater.hydrodynamics.registry.worldgen;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.registry.HydrodynamicsModBlockTags;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

import static net.minecraft.world.level.levelgen.placement.BiomeFilter.biome;
import static net.minecraft.world.level.levelgen.placement.HeightmapPlacement.onHeightmap;
import static net.minecraft.world.level.levelgen.placement.InSquarePlacement.spread;

public interface HydrodynamicsModPlacements {
    private static ResourceKey<PlacedFeature> create(String path) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Hydrodynamics.id(path));
    }

    ResourceKey<PlacedFeature> TIDE_POOL = create("tide_pool");
    ResourceKey<PlacedFeature> TIDE_POOL_AIR_VEGETATION = create("tide_pool_air_vegetation");

    static void bootstrap(BootstrapContext<PlacedFeature> registries) {
        var features = registries.lookup(Registries.FEATURE);

        registries.register(TIDE_POOL, new PlacedFeature(features.getOrThrow(HydrodynamicsModFeatures.TIDE_POOL), List.of(
                CountPlacement.of(4),
                spread(),
                onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                biome()
        )));

        registries.register(TIDE_POOL_AIR_VEGETATION, new PlacedFeature(features.getOrThrow(HydrodynamicsModFeatures.TIDE_POOL_AIR_VEGETATION), List.of(
                CountPlacement.of(16),
                spread(),
                onHeightmap(Heightmap.Types.WORLD_SURFACE_WG),
                biome(),
                BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                        BlockPredicate.matchesTag(Direction.DOWN, HydrodynamicsModBlockTags.TIDE_POOLS_AIR_VEGETATION_BASE),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE
                ))
        )));
    }
}
