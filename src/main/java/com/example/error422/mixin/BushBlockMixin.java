package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Stem/cicek/fidan gibi BushBlock tabanli bitkilerin her blokta kalabilmesi.
 */
@Mixin(BushBlock.class)
public abstract class BushBlockMixin {

    @Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
    private void error422$canSurvive(BlockState state, LevelReader level, BlockPos pos,
                                     CallbackInfoReturnable<Boolean> cir) {
        if (Error422Config.ENABLE_ANYWHERE_CROPS.get() || Error422Config.ENABLE_FLOATING_FLORA.get()) {
            cir.setReturnValue(true);
        }
    }
}
