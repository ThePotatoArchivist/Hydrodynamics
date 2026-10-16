package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface HydrodynamicsModBlockTags {
    private static TagKey<Block> create(String path) {
        return TagKey.create(Registries.BLOCK, Hydrodynamics.id(path));
    }

    TagKey<Block> DEAD_CORAL_BLOCKS = create("dead_coral_blocks");
    TagKey<Block> TIDE_POOLS_AIR_VEGETATION_BASE = create("tide_pools_air_vegetation_base");
}
