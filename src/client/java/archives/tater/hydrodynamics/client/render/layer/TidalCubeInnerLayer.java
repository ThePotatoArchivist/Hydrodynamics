package archives.tater.hydrodynamics.client.render.layer;

import archives.tater.hydrodynamics.client.HydrodynamicsModModelLayers;
import archives.tater.hydrodynamics.client.model.TidalCubeModel;
import archives.tater.hydrodynamics.client.render.TidalCubeRenderer;
import archives.tater.hydrodynamics.client.render.state.TidalCubeRenderState;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class TidalCubeInnerLayer extends RenderLayer<TidalCubeRenderState, TidalCubeModel> {
    private final TidalCubeModel model;

    public TidalCubeInnerLayer(final RenderLayerParent<TidalCubeRenderState, TidalCubeModel> renderer, final EntityModelSet modelSet) {
        super(renderer);
        model = new TidalCubeModel(modelSet.bakeLayer(HydrodynamicsModModelLayers.TIDAL_CUBE_INNER));
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final TidalCubeRenderState state, final float yRot, final float xRot) {
        if (state.isInvisible || state.hasPassenger) return;

        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        submitNodeCollector.order(2).submitModel(
                model,
                state,
                poseStack,
                RenderTypes.entitySolid(TidalCubeRenderer.TEXTURE),
                lightCoords,
                overlayCoords,
                state.outlineColor
        );
    }
}
