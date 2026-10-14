package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.entity.TidalCube;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public interface HydrodynamicsModEntities {
    private static <T extends Entity> EntityType<T> register(ResourceKey<EntityType<?>> key, EntityType.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    EntityType<TidalCube> TIDAL_CUBE = register(HydrodynamicsModEntityIds.TIDAL_CUBE, EntityType.Builder.of(TidalCube::new, MobCategory.MONSTER)
            .sized(0.49F, 0.49F)
            .eyeHeight(0.175F)
            .spawnDimensionsScale(2.0F)
            .clientTrackingRange(10)
    );

    static void init() {
        FabricDefaultAttributeRegistry.register(TIDAL_CUBE, TidalCube.createSulfurCubeAttributes());
    }
}

