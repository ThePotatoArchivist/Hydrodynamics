package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface HydrodynamicsModEntityTags {

    static TagKey<EntityType<?>> create(String path) {
        return TagKey.create(Registries.ENTITY_TYPE, Hydrodynamics.id(path));
    }

    TagKey<EntityType<?>> TIDAL_CUBE_CAN_HOLD = create("tidal_cube_can_hold");
    TagKey<EntityType<?>> REQUIRES_DOUBLE_SIZE_TIDAL_CUBE = create("requires_double_size_tidal_cube");
}
