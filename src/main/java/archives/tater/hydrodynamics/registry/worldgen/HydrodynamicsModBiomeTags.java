package archives.tater.hydrodynamics.registry.worldgen;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public interface HydrodynamicsModBiomeTags {

    private static TagKey<Biome> create(String path) {
        return TagKey.create(Registries.BIOME, Hydrodynamics.id(path));
    }

    TagKey<Biome> ALLOWS_FISH_SPAWNS_SHALLOW = create("allows_fish_spawns_shallow");
}
