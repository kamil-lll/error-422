package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Vanilla muzik yoneticisinin parca baslatmasini tamamen engeller.
 * Glitch sesi kendi SoundInstance'i oldugu icin etkilenmez.
 */
@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {

    @Inject(method = "startPlaying", at = @At("HEAD"), cancellable = true)
    private void error422$startPlaying(Music music, CallbackInfo ci) {
        if (Error422Config.REPLACE_MUSIC.get()) {
            ci.cancel();
        }
    }
}
