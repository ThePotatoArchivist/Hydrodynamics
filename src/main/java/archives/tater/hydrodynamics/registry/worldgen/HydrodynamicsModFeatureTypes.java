package archives.tater.hydrodynamics.registry.worldgen;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.feature.EntityFeature;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;

public class HydrodynamicsModFeatureTypes {
    private static void register(String path, MapCodec<? extends Feature> codec) {
        Registry.register(BuiltInRegistries.FEATURE_TYPE, Hydrodynamics.id(path), codec);
    }

    public static void init() {
        register("entity", EntityFeature.CODEC);
    }
}
