package archives.tater.hydrodynamics.client.render;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.client.HydrodynamicsModModelLayers;
import archives.tater.hydrodynamics.client.model.TidalCubeModel;
import archives.tater.hydrodynamics.client.render.layer.TidalCubeInnerLayer;
import archives.tater.hydrodynamics.client.render.layer.TidalCubeOuterLayer;
import archives.tater.hydrodynamics.client.render.state.TidalCubeRenderState;
import archives.tater.hydrodynamics.entity.TidalCube;

import net.minecraft.client.renderer.entity.AbstractCubeMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

public class TidalCubeRenderer extends AbstractCubeMobRenderer<TidalCube, TidalCubeRenderState, TidalCubeModel> {
    public static final Identifier TEXTURE = Hydrodynamics.id("textures/entity/tidal_cube.png");

    public TidalCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new TidalCubeModel(context.bakeLayer(HydrodynamicsModModelLayers.TIDAL_CUBE)));
        this.addLayer(new TidalCubeOuterLayer(this, context.getModelSet()));
        this.addLayer(new TidalCubeInnerLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(TidalCubeRenderState state) {
        return TEXTURE;
    }

    @Override
    public TidalCubeRenderState createRenderState() {
        return new TidalCubeRenderState();
    }

    @Override
    public void extractRenderState(TidalCube entity, TidalCubeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.hasPassenger = !entity.getPassengers().isEmpty();
    }
}
