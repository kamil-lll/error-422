package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Ekinlerin her blokta kalabilmesi icin canSurvive kontrolunu gevsetir.
 */
@Mixin(CropBlock.class)
public abstract class CropBlockMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void error422$canSurvive(BlockState state, LevelReader level, BlockPos pos,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (Error422Config.ENABLE_ANYWHERE_CROPS.get()) {
            cir.setReturnValue(true);
        }
    }
}
