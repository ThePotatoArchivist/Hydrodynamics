package archives.tater.hydrodynamics.client.render;

import archives.tater.hydrodynamics.client.model.TidalCubeModel;
import archives.tater.hydrodynamics.client.render.state.TidalCubeRenderState;

import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class TidalCubeEyesLayer extends EyesLayer<TidalCubeRenderState, TidalCubeModel> {
    public TidalCubeEyesLayer(RenderLayerParent<TidalCubeRenderState, TidalCubeModel> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return RenderTypes.eyes(TidalCubeRenderer.TEXTURE);
    }
}
