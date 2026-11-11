package com.example.error422.mixin;

import com.example.error422.Error422StackLimit;
import com.example.error422.config.Error422Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Rastgele maksimum stack limiti (creepypasta'daki "her oturumda yeni limit" detayi).
 * Tool/armor gibi vanilla limiti 1 olan esyalar 1 kalir.
 */
@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getMaxStackSize", at = @At("HEAD"), cancellable = true)
    private void error422$getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        boolean enabled;
        try {
            enabled = Error422Config.ENABLE_RANDOM_STACK_LIMIT.get();
        } catch (Exception ignored) {
            enabled = false;
        }
        if (!enabled) {
            return;
        }
        ItemStack stack = (ItemStack) (Object) this;
        Integer vanillaLimit = stack.get(DataComponents.MAX_STACK_SIZE);
        if (vanillaLimit == null) {
            vanillaLimit = 64;
        }
        if (vanillaLimit <= 1) {
            return;
        }
        cir.setReturnValue(Error422StackLimit.get());
    }
}
