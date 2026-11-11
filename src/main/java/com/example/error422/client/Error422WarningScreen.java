package com.example.error422.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

/**
 * Oyun acilisinda ana menuden once gosterilen fotosensitivite uyarisi.
 * Sabittir; yanip sonme/animasyon icermez.
 */
public class Error422WarningScreen extends Screen {

    public Error422WarningScreen() {
        super(Component.translatable("error422.warning.header"));
    }

    @Override
    protected void init() {
        this.addRenderableWidget(Button.builder(
                        Component.translatable("error422.warning.button"),
                        button -> this.onClose())
                .bounds(this.width / 2 - 110, this.height - 56, 220, 20)
                .build());
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Kendi sade arka planimizi ciziyoruz; vanilla panorama/dirt gostermiyoruz.
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int width = this.width;
        int height = this.height;

        guiGraphics.fill(0, 0, width, height, 0xFF0A0A0E);
        guiGraphics.fill(0, 0, width, 3, 0xFFB02020);
        guiGraphics.fill(0, height - 3, width, height, 0xFFB02020);
        for (int y = 0; y < height; y += 4) {
            guiGraphics.fill(0, y, width, y + 1, 0x22000000);
        }

        int centerX = width / 2;
        int y = Math.max(22, height / 5);

        guiGraphics.drawCenteredString(this.font, "ERROR 422", centerX, y, 0xFF802020);
        y += 14;
        guiGraphics.drawCenteredString(this.font, Component.translatable("error422.warning.header"),
                centerX, y, 0xFFFF4040);
        y += 24;

        Component[] lines = {
                Component.translatable("error422.warning.line1"),
                Component.translatable("error422.warning.line2"),
                Component.translatable("error422.warning.line3"),
                Component.translatable("error422.warning.line4"),
                Component.translatable("error422.warning.line5"),
                Component.translatable("error422.warning.line6"),
                Component.translatable("error422.warning.line7"),
                Component.translatable("error422.warning.line8"),
                Component.translatable("error422.warning.line9")
        };

        for (Component line : lines) {
            guiGraphics.drawCenteredString(this.font, line, centerX, y, 0xFFE0E0E0);
            y += 12;
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(new TitleScreen());
    }
}
