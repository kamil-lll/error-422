package com.example.error422.mixin;

import com.example.error422.client.Error422ClientState;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * X-ray flash: birkac tick boyunca katı/cutout terrain katmanlarini atlar.
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Inject(method = "renderSectionLayer", at = @At("HEAD"), cancellable = true)
    private void error422$renderSectionLayer(RenderType type, double x, double y, double z,
                                             Matrix4f pose, Matrix4f projection, CallbackInfo ci) {
        if (Error422ClientState.xray
                && (type == RenderType.solid()
                || type == RenderType.cutout()
                || type == RenderType.cutoutMipped())) {
            ci.cancel();
        }
    }
}
