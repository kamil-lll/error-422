package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Ucan kaktusler: kaktuslerin her yerde canli kalmasini saglar.
 */
@Mixin(CactusBlock.class)
public abstract class CactusBlockMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void error422$canSurvive(BlockState state, LevelReader level, BlockPos pos,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (Error422Config.ENABLE_FLOATING_FLORA.get()) {
            cir.setReturnValue(true);
        }
    }
}
