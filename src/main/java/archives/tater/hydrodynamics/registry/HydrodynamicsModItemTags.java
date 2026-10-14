package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface HydrodynamicsModItemTags {
    static TagKey<Item> create(String path) {
        return TagKey.create(Registries.ITEM, Hydrodynamics.id(path));
    }

    TagKey<Item> DEFAULT_TIDAL_CUBE_FOOD_LOW = create("default_tidal_cube_food/low");
    TagKey<Item> DEFAULT_TIDAL_CUBE_FOOD_MEDIUM = create("default_tidal_cube_food/medium");
    TagKey<Item> DEFAULT_TIDAL_CUBE_FOOD_HIGH = create("default_tidal_cube_food/high");
    TagKey<Item> DEFAULT_TIDAL_CUBE_FOOD_ALWAYS_REUSABLE = create("default_tidal_cube_food/always_reusable");
}
