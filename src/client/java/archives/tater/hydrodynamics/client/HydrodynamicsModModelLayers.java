package archives.tater.hydrodynamics.client;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.client.model.TidalCubeModel;

import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

import net.minecraft.client.model.geom.ModelLayerLocation;

public interface HydrodynamicsModModelLayers {
    private static ModelLayerLocation create(String name) {
        return create(name, "main");
    }

    private static ModelLayerLocation create(String name, String layer) {
        return new ModelLayerLocation(Hydrodynamics.id(name), layer);
    }

    ModelLayerLocation TIDAL_CUBE = create("tidal_cube");
    ModelLayerLocation TIDAL_CUBE_EYES = create("tidal_cube_eyes");
    ModelLayerLocation TIDAL_CUBE_OUTER = create("tidal_cube", "outer");
    ModelLayerLocation TIDAL_CUBE_INNER = create("tidal_cube", "inner");

    static void init() {
        ModelLayerRegistry.registerModelLayer(TIDAL_CUBE, TidalCubeModel::createEyesLayer);
        ModelLayerRegistry.registerModelLayer(TIDAL_CUBE_EYES, TidalCubeModel::createEyesLayer);
        ModelLayerRegistry.registerModelLayer(TIDAL_CUBE_OUTER, TidalCubeModel::createOuterBodyLayer);
        ModelLayerRegistry.registerModelLayer(TIDAL_CUBE_INNER, TidalCubeModel::createInnerBodyLayer);
    }
}
