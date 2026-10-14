package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;

public interface HydrodynamicsModEntityIds {
    private static ResourceKey<EntityType<?>> create(String path) {
        return ResourceKey.create(Registries.ENTITY_TYPE, Hydrodynamics.id(path));
    }

    ResourceKey<EntityType<?>> TIDAL_CUBE = create("tidal_cube");
}
