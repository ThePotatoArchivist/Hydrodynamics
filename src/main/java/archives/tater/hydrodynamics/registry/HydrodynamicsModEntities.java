package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.entity.TidalCube;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.levelgen.Heightmap;

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

    @SuppressWarnings("DataFlowIssue") // Intellij seems to be bugging
    static void init() {
        SpawnPlacements.register(TIDAL_CUBE, SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TidalCube::checkTidalCubeSpawnRules);
        FabricDefaultAttributeRegistry.register(TIDAL_CUBE, TidalCube.createTidalCubeAttributes());
    }
}

