package com.example.error422.mixin;

import com.example.error422.client.Error422ClientState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Envanter yuklenmeme glitch'i: slot icerikleri bir sure bos gorunur.
 */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Inject(method = "renderSlot", at = @At("HEAD"), cancellable = true)
    private void error422$renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (Error422ClientState.inventoryGlitchTicks > 0) {
            ci.cancel();
        }
    }
}
