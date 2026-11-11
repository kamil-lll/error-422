package com.example.error422.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;

/**
 * Sahte cokme ekrani. Gercek oyunu kapatmaz; kisa sureligine kontrolu kilitler.
 */
public class Error422CrashScreen extends Screen {

    private final RandomSource random = RandomSource.create();
    private int ticks;

    public Error422CrashScreen() {
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
        this.ticks++;
        if (this.ticks > 80) {
            this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int width = this.width;
        int height = this.height;
        guiGraphics.fill(0, 0, width, height, 0xFF120000);

        for (int i = 0; i < 22; i++) {
            int y = this.random.nextInt(height);
            int lineHeight = 1 + this.random.nextInt(6);
            int color = switch (this.random.nextInt(4)) {
                case 0 -> 0x66FF0000;
                case 1 -> 0x6600FFFF;
                case 2 -> 0x44FFFFFF;
                default -> 0x33000000;
            };
            guiGraphics.fill(0, y, width, Math.min(height, y + lineHeight), color);
        }

        for (int y = 0; y < height; y += 4) {
            guiGraphics.fill(0, y, width, y + 1, 0x33000000);
        }

        String title = "ERROR 422";
        int centerX = width / 2;
        int centerY = height / 2;
        guiGraphics.drawCenteredString(this.font, title, centerX + 2, centerY - 12, 0xFF00FFFF);
        guiGraphics.drawCenteredString(this.font, title, centerX - 2, centerY - 12, 0xFFFF0000);
        guiGraphics.drawCenteredString(this.font, title, centerX, centerY - 12, 0xFFFFFFFF);

        String[] lines = {
                "§cjava.lang.NullPointerException: null",
                "§8    at net.minecraft.client.Minecraft.run(Unknown Source)",
                "§8    at §kXXXXXXXXXXXXXXXXXXXXXXXX",
                "§c§kERRORERRORERRORERROR"
        };
        for (int i = 0; i < lines.length; i++) {
            guiGraphics.drawString(this.font, lines[i], Math.max(10, centerX - 160),
                    centerY + 8 + i * 12, 0xFFFF5555);
        }
        guiGraphics.drawString(this.font, Component.translatable("error422.crash.control"),
                Math.max(10, centerX - 160), centerY + 8 + lines.length * 12, 0xFFFF5555);

        if ((this.ticks / 10) % 2 == 0) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("error422.crash.terminated"),
                    centerX, centerY + 84, 0xFFFF2222);
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
