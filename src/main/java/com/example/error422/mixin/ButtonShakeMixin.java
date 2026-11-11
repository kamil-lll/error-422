package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Ana menude fare ustune gelince butonu titretir.
 */
@Mixin(AbstractButton.class)
public abstract class ButtonShakeMixin {

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void error422$renderWidgetHead(GuiGraphics guiGraphics, int mouseX, int mouseY,
                                           float partialTick, CallbackInfo ci) {
        guiGraphics.pose().pushPose();
        if (Error422Config.ENABLE_BUTTON_SHAKE.get()
                && Minecraft.getInstance().screen instanceof TitleScreen
                && ((AbstractButton) (Object) this).isHoveredOrFocused()) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            guiGraphics.pose().translate((random.nextFloat() - 0.5F) * 3.0F,
                    (random.nextFloat() - 0.5F) * 2.0F, 0.0F);
        }
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void error422$renderWidgetTail(GuiGraphics guiGraphics, int mouseX, int mouseY,
                                           float partialTick, CallbackInfo ci) {
        guiGraphics.pose().popPose();
    }
}
