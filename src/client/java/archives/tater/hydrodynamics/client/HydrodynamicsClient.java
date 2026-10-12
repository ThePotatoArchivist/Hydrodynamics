package archives.tater.hydrodynamics.client;

import archives.tater.hydrodynamics.client.render.TidalCubeRenderer;
import archives.tater.hydrodynamics.registry.HydrodynamicsModEntities;

import net.fabricmc.api.ClientModInitializer;

import net.minecraft.client.renderer.entity.EntityRenderers;

public class HydrodynamicsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		HydrodynamicsModModelLayers.init();
		EntityRenderers.register(HydrodynamicsModEntities.TIDAL_CUBE, TidalCubeRenderer::new);
	}
}