package com.example.error422.client;

import net.minecraft.client.sounds.AudioStream;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Sonsuz, sentezlenmis glitch sesi: beyaz gurultu + dusuk ugultu + bozuk sinyal patlamalari.
 * Dosya gerektirmez; SoundEngine bu akisi surekli okur.
 */
public class GlitchAudioStream implements AudioStream {

    private static final AudioFormat FORMAT = new AudioFormat(44100.0F, 16, 1, true, false);
    private static final double TWO_PI = Math.PI * 2.0D;

    private final Random random = new Random();
    private double humPhase;
    private int burstRemaining;
    private double burstFrequency;
    private int whineRemaining;

    @Override
    public AudioFormat getFormat() {
        return FORMAT;
    }

    @Override
    public ByteBuffer read(int size) {
        int samples = Math.max(1, size / 2);
        ByteBuffer buffer = ByteBuffer.allocateDirect(samples * 2).order(ByteOrder.LITTLE_ENDIAN);

        for (int i = 0; i < samples; i++) {
            double value = (this.random.nextDouble() * 2.0D - 1.0D) * 0.16D;

            this.humPhase += TWO_PI * 47.0D / 44100.0D;
            if (this.humPhase > TWO_PI) {
                this.humPhase -= TWO_PI;
            }
            value += Math.sin(this.humPhase) * 0.11D;

            if (this.burstRemaining <= 0 && this.random.nextInt(14000) == 0) {
                this.burstRemaining = 300 + this.random.nextInt(1700);
                this.burstFrequency = 90.0D + this.random.nextDouble() * 3200.0D;
            }
            if (this.burstRemaining > 0) {
                double square = Math.sin(this.humPhase * this.burstFrequency / 47.0D) >= 0.0D ? 1.0D : -1.0D;
                value += square * 0.22D * Math.min(1.0D, this.burstRemaining / 1200.0D);
                this.burstRemaining--;
            }

            if (this.whineRemaining <= 0 && this.random.nextInt(44100 * 7) == 0) {
                this.whineRemaining = 500 + this.random.nextInt(900);
            }
            if (this.whineRemaining > 0) {
                value += Math.sin(this.humPhase * 8200.0D / 47.0D) * 0.045D;
                this.whineRemaining--;
            }

            double clamped = Math.max(-1.0D, Math.min(1.0D, value));
            buffer.putShort((short) (clamped * 32767.0D));
        }

        buffer.flip();
        return buffer;
    }

    @Override
    public void close() {
    }
}
