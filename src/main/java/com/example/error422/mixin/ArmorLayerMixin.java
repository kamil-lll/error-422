package com.example.error422.mixin;

import com.example.error422.client.Error422ClientState;
import com.example.error422.config.Error422Config;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Bassiz oyuncuda kasklarin da gorunmemesi icin helmet katmanini atlar.
 */
@Mixin(HumanoidArmorLayer.class)
public abstract class ArmorLayerMixin {

    @Inject(method = "renderArmorPiece", at = @At("HEAD"), cancellable = true)
    private void error422$renderArmorPiece(PoseStack poseStack, MultiBufferSource buffer, LivingEntity entity,
                                           EquipmentSlot slot, int packedLight, HumanoidModel<?> model,
                                           CallbackInfo ci) {
        if (Error422Config.ENABLE_HEADLESS_PLAYER.get()
                && Error422ClientState.headlessPlayer
                && entity instanceof Player
                && slot == EquipmentSlot.HEAD) {
            ci.cancel();
        }
    }
}
