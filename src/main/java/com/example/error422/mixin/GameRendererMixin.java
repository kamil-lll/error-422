package com.example.error422.mixin;

import com.example.error422.client.Error422ClientState;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Bizim anaglyph efektimiz aktifken vanilla checkEntityPostEffect'in
 * efekti kapatmasini engeller.
 */
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Inject(method = "checkEntityPostEffect", at = @At("HEAD"), cancellable = true)
    private void error422$checkEntityPostEffect(Entity entity, CallbackInfo ci) {
        if (Error422ClientState.anaglyph) {
            ci.cancel();
        }
    }
}
