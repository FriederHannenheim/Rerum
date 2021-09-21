// Made with Model Converter by Globox_Z
// Generate all required imports
// Made with Blockbench 3.8.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
package fhannenheim.rerum.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public class BootModel extends BipedEntityModel<PlayerEntity> {
    private final ModelPart rightLeg;
    private final ModelPart RightRocket;
    private final ModelPart leftLeg;
    private final ModelPart LeftRocket;

    public BootModel(ModelPart root) {
        super(root);
        root =getTexturedModelData().createModel();
        this.rightLeg = root.getChild("right_leg");
        this.RightRocket = this.rightLeg.getChild("RightRocket");
        this.leftLeg = root.getChild("left_leg");
        this.LeftRocket = this.leftLeg.getChild("LeftRocket");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("right_leg", ModelPartBuilder.create(), ModelTransform.pivot(-1.9F, 12.0F, 0.0F));
        modelPartData1.addChild("RightRocket", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F).uv(12, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F).uv(12, 3).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F).uv(12, 5).cuboid(1.5F, 2.0F, -0.5F, 2.0F, 1.0F, 1.0F), ModelTransform.pivot(-4.2F, 7.5F, 0.0F));
        ModelPartData modelPartData2 = modelPartData.addChild("left_leg", ModelPartBuilder.create(), ModelTransform.pivot(1.9F, 12.0F, 0.0F));
        modelPartData2.addChild("LeftRocket", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, 0.0F, -1.5F, 3.0F, 4.0F, 3.0F).uv(12, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 2.0F).uv(12, 3).cuboid(-0.5F, -2.0F, -0.5F, 1.0F, 1.0F, 1.0F).uv(12, 5).cuboid(-3.2F, 2.0F, -0.5F, 2.0F, 1.0F, 1.0F), ModelTransform.pivot(4.2F, 7.5F, 0.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(PlayerEntity entity, float f, float g, float h, float i, float j) {
        boolean bl = entity.getRoll() > 4;
        float k = 1.0F;
        if (bl) {
            k = (float)entity.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        if (k < 1.0F) {
            k = 1.0F;
        }

        this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
        this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + 3.1415927F) * 1.4F * g / k;
        this.rightLeg.yaw = 0.0F;
        this.leftLeg.yaw = 0.0F;
        this.rightLeg.roll = 0.0F;
        this.leftLeg.roll = 0.0F;

        if (this.riding) {
            this.rightLeg.pitch = -1.4137167F;
            this.rightLeg.yaw = 0.31415927F;
            this.rightLeg.roll = 0.07853982F;
            this.leftLeg.pitch = -1.4137167F;
            this.leftLeg.yaw = -0.31415927F;
            this.leftLeg.roll = -0.07853982F;
        }

        if (this.sneaking) {
            this.rightLeg.pivotZ = 4.0F;
            this.leftLeg.pivotZ = 4.0F;
            this.rightLeg.pivotY = 12.2F;
            this.leftLeg.pivotY = 12.2F;
        } else {
            this.rightLeg.pivotZ = 0.1F;
            this.leftLeg.pivotZ = 0.1F;
            this.rightLeg.pivotY = 12.0F;
            this.leftLeg.pivotY = 12.0F;
        }

        if (this.leaningPitch > 0.0F) {
            this.leftLeg.pitch = MathHelper.lerp(this.leaningPitch, this.leftLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F + 3.1415927F));
            this.rightLeg.pitch = MathHelper.lerp(this.leaningPitch, this.rightLeg.pitch, 0.3F * MathHelper.cos(f * 0.33333334F));
        }

    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rightLeg.render(matrixStack, buffer, packedLight, packedOverlay);
        leftLeg.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}