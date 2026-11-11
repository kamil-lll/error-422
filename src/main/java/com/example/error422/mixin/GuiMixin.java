package com.example.error422.mixin;

import com.example.error422.client.Error422ClientState;
import com.example.error422.config.Error422Config;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Bozuk can/aciklik barlari, eksik oksijen barı ve binary XP.
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    private void error422$renderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!Error422Config.ENABLE_HUD_CORRUPTION.get()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        ci.cancel();

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int left = width / 2 - 91;
        int y = height - 39;
        long phase = System.currentTimeMillis() / 120L;

        for (int i = 0; i < 10; i++) {
            int color = ((i + (int) phase) % 3 == 0) ? 0xFFFF2020 : 0xFF330000;
            guiGraphics.fill(left + i * 8, y, left + i * 8 + 7, y + 8, color);
        }
        for (int i = 0; i < 10; i++) {
            int color = ((i + (int) phase) % 4 == 0) ? 0xFFAA5500 : 0xFF2A1400;
            guiGraphics.fill(width / 2 + 91 - i * 8 - 7, y, width / 2 + 91 - i * 8, y + 8, color);
        }
        guiGraphics.drawString(minecraft.font, "§c§kERR422§r", width / 2 - 26, y - 11, 0xFFFF4040);
        guiGraphics.drawString(minecraft.font, "§c??", width / 2 - 6, y - 1, 0xFFFF3030);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void error422$render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!Error422ClientState.xray) {
            return;
        }
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        guiGraphics.fill(0, 0, width, height, 0x22AAFFAA);
        for (int y = 0; y < height; y += 6) {
            guiGraphics.fill(0, y, width, y + 1, 0x44AAFFAA);
        }
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void error422$renderExperienceBar(GuiGraphics guiGraphics, int x, CallbackInfo ci) {
        if (!Error422Config.ENABLE_BINARY_XP.get()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) {
            return;
        }
        ci.cancel();

        String binary = Integer.toBinaryString(Math.max(0, minecraft.player.experienceLevel));
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int textWidth = minecraft.font.width(binary);
        guiGraphics.drawString(minecraft.font, "§a" + binary,
                width / 2 - textWidth / 2, height - 31, 0xFF55FF55);
        guiGraphics.fill(width / 2 - 91, height - 29, width / 2 + 91, height - 24, 0xFF003300);
        int progress = (int) (182.0F * minecraft.player.experienceProgress);
        guiGraphics.fill(width / 2 - 91, height - 29, width / 2 - 91 + progress, height - 24, 0xFF00AA00);
    }
}
