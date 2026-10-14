package archives.tater.hydrodynamics.registry;

import archives.tater.hydrodynamics.Hydrodynamics;

import net.fabricmc.fabric.api.dimension.v1.DimensionEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;

public interface HydrodynamicsModEnvironment {
    private static <T> EnvironmentAttribute<T> register(String path, EnvironmentAttribute.Builder<T> builder) {
        return Registry.register(BuiltInRegistries.ENVIRONMENT_ATTRIBUTE, Hydrodynamics.id(path), builder.build());
    }

    EnvironmentAttribute<Boolean> TIDAL_CUBE_EVAPORATES = register("gameplay/snow_golem_melts", EnvironmentAttribute.builder(AttributeTypes.BOOLEAN).defaultValue(false));

    static void init() {
        DimensionEvents.MODIFY_ATTRIBUTES.register((dimension, attributes, _) -> {
            if (dimension.is(BuiltinDimensionTypes.NETHER))
                attributes.set(TIDAL_CUBE_EVAPORATES, true);
        });
    }
}
