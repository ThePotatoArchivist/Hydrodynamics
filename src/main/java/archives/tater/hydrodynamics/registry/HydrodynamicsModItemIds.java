package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface HydrodynamicsModItemIds {
    private static ResourceKey<Item> create(String path) {
        return ResourceKey.create(Registries.ITEM, Hydrodynamics.id(path));
    }

    ResourceKey<Item> TIDAL_CUBE_BUCKET = create("tidal_cube_bucket");
}
