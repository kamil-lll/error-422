package com.example.error422.client;

import com.example.error422.Error422Mod;
import com.example.error422.entity.Error422Entity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;

/**
 * Kafasi olmayan humanoid model. Govde, kollar ve bacaklar render edilir.
 */
public class HeadlessHumanoidModel extends HumanoidModel<Error422Entity> {

    public HeadlessHumanoidModel(ModelPart root) {
        super(root);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        this.body.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.rightArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.leftArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.rightLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        this.leftLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
