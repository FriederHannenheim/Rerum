package fhannenheim.rerum.render;

import fhannenheim.rerum.registries.ItemRegistry;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class BoostBootsFeatureRenderer<T extends PlayerEntity, M extends BipedEntityModel<T>> extends FeatureRenderer<T, M>  {
    FeatureRendererContext<T, M> context;
    public BoostBootsFeatureRenderer(FeatureRendererContext<T, M> _context) {
        super(_context);
        context = _context;

    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if(entity.getEquippedStack(EquipmentSlot.FEET).getItem() != ItemRegistry.BOOST_BOOTS)
            return;

        VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(context.getTexture(entity)), true, false);
        M model = context.getModel();
        model.setAngles(entity,limbAngle,limbDistance,animationProgress,headYaw,headPitch);
        model.render(matrices, vertexConsumer, light, OverlayTexture.field_32955, 1f, 1f, 1f, 1f);
    }
}
