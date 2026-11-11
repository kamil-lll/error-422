package com.example.error422.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Varlik kayboldugunda kisa sureli kirmizi overlay.
 */
public class Error422VanishScreen extends Screen {

    private int ticks;

    public Error422VanishScreen() {
        super(Component.literal("ERROR 422"));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public void tick() {
        if (++this.ticks > 30) {
            this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int width = this.width;
        int height = this.height;
        guiGraphics.fill(0, 0, width, height, 0x55FF0000);

        long phase = System.currentTimeMillis() / 35L;
        for (int i = 0; i < 18; i++) {
            int y = (int) ((phase * 11L + i * 43L) % Math.max(1, height));
            guiGraphics.fill(0, y, width, Math.min(height, y + 2), 0x44FF1010);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }
}
