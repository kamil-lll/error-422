package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SplashRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ana menudeki sari splash yazisini kaldirir.
 */
@Mixin(SplashRenderer.class)
public abstract class SplashRendererMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void error422$render(GuiGraphics guiGraphics, int screenWidth, Font font, int color, CallbackInfo ci) {
        if (Error422Config.HIDE_SPLASH.get()) {
            ci.cancel();
        }
    }
}
