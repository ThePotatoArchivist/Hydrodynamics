package archives.tater.hydrodynamics.client.render;

import archives.tater.hydrodynamics.Hydrodynamics;
import archives.tater.hydrodynamics.client.HydrodynamicsClient;
import archives.tater.hydrodynamics.client.render.layer.TidalCubeOuterLayer;
import archives.tater.hydrodynamics.entity.TidalCube;

import net.minecraft.client.model.monster.slime.SlimeModel;
import net.minecraft.client.renderer.entity.AbstractCubeMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;
import net.minecraft.resources.Identifier;

public class TidalCubeRenderer extends AbstractCubeMobRenderer<TidalCube, SlimeRenderState, SlimeModel> {
    public static final Identifier TEXTURE = Hydrodynamics.id("textures/entity/tidal_cube.png");

    public TidalCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel(context.bakeLayer(HydrodynamicsClient.TIDAL_CUBE)));
        this.addLayer(new TidalCubeOuterLayer(this, context.getModelSet()));
    }

    @Override
    public Identifier getTextureLocation(SlimeRenderState state) {
        return TEXTURE;
    }

    @Override
    public SlimeRenderState createRenderState() {
        return new SlimeRenderState();
    }
}
