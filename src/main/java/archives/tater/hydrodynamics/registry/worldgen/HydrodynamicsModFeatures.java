package archives.tater.hydrodynamics.registry.worldgen;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.registry.HydrodynamicsModBlockTags;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.WaterloggedVegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.WeightedRandomSelectorFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomBlockProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import dev.worldgen.lithostitched.impl.worldgen.stateprovider.WeightedProvider;

import java.util.List;

public interface HydrodynamicsModFeatures {
    private static ResourceKey<Feature> create(String path) {
        return ResourceKey.create(Registries.FEATURE, Hydrodynamics.id(path));
    }

    ResourceKey<Feature> TIDE_POOL = create("tide_pool");
    ResourceKey<Feature> TIDE_POOL_WATER_VEGETATION = create("tide_pool_water_vegetation");
    ResourceKey<Feature> TIDE_POOL_AIR_VEGETATION = create("tide_pool_air_vegetation");

    static void bootstrap(BootstrapContext<Feature> registries) {
        var blocks = registries.lookup(Registries.BLOCK);
        var placedFeatures = registries.lookup(Registries.PLACED_FEATURE);

        var tidePoolWaterVegetation = registries.register(TIDE_POOL_WATER_VEGETATION, new WeightedRandomSelectorFeature(WeightedList.<Holder<PlacedFeature>>builder()
                .add(simplePlacement(Holder.direct(new SimpleBlockFeature(BlockStateProvider.of(Blocks.SEAGRASS)))), 5)
                .add(simplePlacement(Holder.direct(new SimpleBlockFeature(BlockStateProvider.of(Blocks.KELP)))), 3)
                .add(simplePlacement(Holder.direct(new SimpleBlockFeature(new RandomBlockProvider(blocks.getOrThrow(BlockTags.CORALS))))), 3)
                .add(simplePlacement(Holder.direct(new SimpleBlockFeature(BlockStateProvider.of(Blocks.SEA_PICKLE)))))
                .build()));

        var corals = new WeightedList.Builder<BlockState>();
        corals.add(nonWaterlogged(Blocks.DEAD_TUBE_CORAL));
        corals.add(nonWaterlogged(Blocks.DEAD_BRAIN_CORAL));
        corals.add(nonWaterlogged(Blocks.DEAD_BUBBLE_CORAL));
        corals.add(nonWaterlogged(Blocks.DEAD_FIRE_CORAL));
        corals.add(nonWaterlogged(Blocks.DEAD_HORN_CORAL));
        registries.register(TIDE_POOL_AIR_VEGETATION, new SimpleBlockFeature(new WeightedStateProvider(corals.build())));

        registries.register(TIDE_POOL, new WaterloggedVegetationPatchFeature(
                blockSet(Blocks.SAND, Blocks.DIRT),
                Holder.direct(new WeightedProvider(WeightedList.<Holder<BlockStateProvider>>builder()
                        .add(Holder.direct(new RandomBlockProvider(blocks.getOrThrow(HydrodynamicsModBlockTags.DEAD_CORAL_BLOCKS))), 1)
                        .add(Holder.direct(BlockStateProvider.of(Blocks.DRIPSTONE_BLOCK)), 1)
                        .build())),
                simplePlacement(tidePoolWaterVegetation),
                CaveSurface.FLOOR,
                ConstantInt.of(3),
                0.8f,
                5,
                0.5f,
                UniformInt.of(4, 7),
                0.7f
        ));
    }

    private static BlockState nonWaterlogged(Block block) {
        return block.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false);
    }

    private static Holder<PlacedFeature> simplePlacement(Holder<Feature> feature) {
        return Holder.direct(new PlacedFeature(feature, List.of()));
    }

    @SuppressWarnings("deprecation")
    private static HolderSet.Direct<Block> blockSet(Block... blocks) {
        return HolderSet.direct(Block::builtInRegistryHolder, blocks);
    }
}
