package com.example.error422.client;

import com.example.error422.config.Error422Config;
import net.minecraft.client.resources.sounds.AbstractSoundInstance;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.concurrent.CompletableFuture;

/**
 * Vanilla muzik yerine sonsuz glitch ambience'i calar.
 * Muzik SoundEvent'ini sadece "streaming" bir tasiyici olarak kullaniriz;
 * gercek ses GlitchAudioStream tarafindan uretilir.
 */
public class GlitchAmbienceSound extends AbstractSoundInstance {

    public GlitchAmbienceSound() {
        super(SoundEvents.MUSIC_GAME.value(), SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.relative = true;
        this.attenuation = SoundInstance.Attenuation.NONE;
        this.volume = (float) (double) Error422Config.GLITCH_VOLUME.get();
        this.pitch = 1.0F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return true;
    }

    @Override
    public CompletableFuture<AudioStream> getStream(SoundBufferLibrary library, Sound sound, boolean looping) {
        return CompletableFuture.completedFuture(new GlitchAudioStream());
    }
}
