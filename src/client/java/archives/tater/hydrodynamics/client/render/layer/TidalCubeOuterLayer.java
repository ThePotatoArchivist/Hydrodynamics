package archives.tater.hydrodynamics.client.render.layer;

import archives.tater.hydrodynamics.client.HydrodynamicsClient;
import archives.tater.hydrodynamics.client.render.TidalCubeRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class TidalCubeOuterLayer extends RenderLayer<SlimeRenderState, SlimeModel> {
    private final SlimeModel model;

    public TidalCubeOuterLayer(final RenderLayerParent<SlimeRenderState, SlimeModel> renderer, final EntityModelSet modelSet) {
        super(renderer);
        this.model = new SlimeModel(modelSet.bakeLayer(HydrodynamicsClient.TIDAL_CUBE_OUTER));
    }

    public void submit(final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final int lightCoords, final SlimeRenderState state, final float yRot, final float xRot) {
        var appearsGlowingWithInvisibility = state.appearsGlowing() && state.isInvisible;
        if (state.isInvisible && !appearsGlowingWithInvisibility) return;

        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        submitNodeCollector.order(1).submitModel(
                this.model,
                state,
                poseStack,
                appearsGlowingWithInvisibility
                        ? RenderTypes.outline(TidalCubeRenderer.TEXTURE)
                        : RenderTypes.entityTranslucent(TidalCubeRenderer.TEXTURE),
                lightCoords,
                overlayCoords,
                state.outlineColor
        );
    }
}
