package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Minecraft logosunu kirmizi/mavi glitch katmanlariyla cizer.
 * "Java Edition" yazisi hic cizilmez; sadece logo kalir.
 */
@Mixin(LogoRenderer.class)
public abstract class LogoRendererMixin {

    @Inject(method = "renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IFI)V", at = @At("HEAD"), cancellable = true)
    private void error422$renderLogo(GuiGraphics guiGraphics, int screenWidth, float transparency, int heightOffset,
                                     CallbackInfo ci) {
        if (!Error422Config.ENABLE_GLITCHED_LOGO.get()) {
            return;
        }
        ci.cancel();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        int x = screenWidth / 2 - 128;
        int y = heightOffset;
        ResourceLocation logo = LogoRenderer.MINECRAFT_LOGO;
        float alpha = transparency;

        // Kirmizi hayalet
        guiGraphics.setColor(1.0F, 0.12F, 0.12F, 0.7F * alpha);
        guiGraphics.blit(logo, x - 2, y, 0.0F, 0.0F, 256, 44, 256, 64);

        // Mavi hayalet
        guiGraphics.setColor(0.15F, 0.35F, 1.0F, 0.7F * alpha);
        guiGraphics.blit(logo, x + 2, y, 0.0F, 0.0F, 256, 44, 256, 64);

        // Normal logo
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, alpha);
        guiGraphics.blit(logo, x, y, 0.0F, 0.0F, 256, 44, 256, 64);

        // Animasyonlu glitch dilimleri
        RandomSource random = RandomSource.create(System.currentTimeMillis() / 90L);
        for (int i = 0; i < 7; i++) {
            int sliceY = random.nextInt(42);
            int sliceHeight = 2 + random.nextInt(7);
            int dx = random.nextInt(9) - 4;
            if (random.nextBoolean()) {
                guiGraphics.setColor(1.0F, 0.2F, 0.2F, 0.65F * alpha);
            } else {
                guiGraphics.setColor(0.2F, 0.4F, 1.0F, 0.65F * alpha);
            }
            guiGraphics.blit(logo, x + dx, y + sliceY, 0.0F, (float) sliceY,
                    256, sliceHeight, 256, 64);
        }

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }
}
