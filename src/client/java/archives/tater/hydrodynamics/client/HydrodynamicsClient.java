package archives.tater.hydrodynamics.client;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.client.render.TidalCubeRenderer;
import archives.tater.hydrodynamics.registry.HydrodynamicsModEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.EntityRenderers;

public class HydrodynamicsClient implements ClientModInitializer {
	public static final ModelLayerLocation TIDAL_CUBE = create("tidal_cube");
	public static final ModelLayerLocation TIDAL_CUBE_OUTER = create("tidal_cube", "outer");

	private static ModelLayerLocation create(String name) {
		return create(name, "main");
	}

	private static ModelLayerLocation create(String name, String layer) {
		return new ModelLayerLocation(Hydrodynamics.id(name), layer);
	}

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		ModelLayerRegistry.registerModelLayer(TIDAL_CUBE, SlimeModel::createInnerBodyLayer);
		ModelLayerRegistry.registerModelLayer(TIDAL_CUBE_OUTER, SlimeModel::createOuterBodyLayer);
		EntityRenderers.register(HydrodynamicsModEntities.TIDAL_CUBE, TidalCubeRenderer::new);
	}
}