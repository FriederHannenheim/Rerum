package fhannenheim.rerum.render;


import fhannenheim.rerum.Rerum;
import fhannenheim.rerum.entities.NetherArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class NetherArrowEntityRenderer extends ProjectileEntityRenderer<NetherArrowEntity> {
    public static final Identifier TEXTURE = new Identifier(Rerum.MOD_ID,"textures/entity/nether_arrow_entity.png");

    public NetherArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(NetherArrowEntity entity) {
        return TEXTURE;
    }
}