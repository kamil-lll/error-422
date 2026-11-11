package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ana menu arka planini animasyonlu glitch'e cevirir: kayan kod dokusu + flicker barlar.
 */
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {

    @Inject(method = "renderPanorama", at = @At("HEAD"), cancellable = true)
    private void error422$renderPanorama(GuiGraphics guiGraphics, float partialTick, CallbackInfo ci) {
        if (!Error422Config.ENABLE_ANIMATED_MENU.get()) {
            return;
        }
        ci.cancel();

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        long now = System.currentTimeMillis();
        ResourceLocation texture = ResourceLocation.withDefaultNamespace(
                "textures/gui/title/background/panorama_0.png");
        int offset = (int) ((now / 18L) % Math.max(1, height));

        // Iki kopya ile dikey kaydirma; kesintisiz animasyon.
        guiGraphics.blit(texture, 0, offset - height, 0.0F, 0.0F, width, height, 1024, 1024);
        guiGraphics.blit(texture, 0, offset, 0.0F, 0.0F, width, height, 1024, 1024);

        RandomSource random = RandomSource.create(now / 55L);
        for (int i = 0; i < 55; i++) {
            int y = random.nextInt(height);
            int lineHeight = 1 + random.nextInt(3);
            int color = switch (random.nextInt(4)) {
                case 0 -> 0x66FF0000;
                case 1 -> 0x6600FFFF;
                case 2 -> 0x44FFFFFF;
                default -> 0x33000000;
            };
            int x = random.nextInt(Math.max(1, width));
            int lineWidth = 40 + random.nextInt(Math.max(1, width / 2));
            guiGraphics.fill(x, y, Math.min(width, x + lineWidth), Math.min(height, y + lineHeight), color);
        }

        for (int y = 0; y < height; y += 4) {
            guiGraphics.fill(0, y, width, y + 1, 0x33000000);
        }
    }
}
