package com.example.error422.mixin;

import com.example.error422.config.Error422Config;
import com.mojang.serialization.Lifecycle;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState.SelectedGameMode;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.charset.StandardCharsets;

/**
 * Dunya adini hex'e cevirir ve dunya olusturmayi survival + cheats kapali ile sinirlar.
 * Hem onCreate hem de gercek olusturma adimi olan createNewWorld'da uygulanir.
 */
@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin {

    @Inject(method = "onCreate", at = @At("HEAD"))
    private void error422$onCreate(CallbackInfo ci) {
        error422$apply((CreateWorldScreen) (Object) this);
    }

    @Inject(method = "createNewWorld", at = @At("HEAD"))
    private void error422$createNewWorld(PrimaryLevelData.SpecialWorldProperty special,
                                         LayeredRegistryAccess<RegistryLayer> registries,
                                         Lifecycle lifecycle,
                                         CallbackInfo ci) {
        error422$apply((CreateWorldScreen) (Object) this);
    }

    @Unique
    private static void error422$apply(CreateWorldScreen screen) {
        WorldCreationUiState state = screen.getUiState();

        if (Error422Config.HEX_WORLD_NAME.get()) {
            String name = state.getName();
            if (name != null && !name.isBlank() && !error422$isHex(name)) {
                state.setName(error422$toHex(name));
            }
        }

        if (Error422Config.FORCE_SURVIVAL.get()) {
            state.setGameMode(SelectedGameMode.SURVIVAL);
            state.setAllowCommands(false);
        }
    }

    @Unique
    private static boolean error422$isHex(String value) {
        if (value.length() < 2 || value.length() % 2 != 0) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (Character.digit(value.charAt(i), 16) < 0) {
                return false;
            }
        }
        return true;
    }

    @Unique
    private static String error422$toHex(String value) {
        StringBuilder builder = new StringBuilder(value.length() * 2);
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return builder.toString();
    }
}
