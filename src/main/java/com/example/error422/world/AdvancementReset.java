package com.example.error422.world;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Oyuncunun tum advancement ilerlemesini gercekten sifirlar.
 */
public final class AdvancementReset {

    private AdvancementReset() {
    }

    public static void reset(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        ServerAdvancementManager manager = server.getAdvancements();
        PlayerAdvancements advancements = player.getAdvancements();

        for (AdvancementHolder holder : manager.getAllAdvancements()) {
            AdvancementProgress progress = advancements.getOrStartProgress(holder);
            List<String> completed = new ArrayList<>();
            progress.getCompletedCriteria().forEach(completed::add);
            for (String criterion : completed) {
                advancements.revoke(holder, criterion);
            }
        }

        advancements.save();
        advancements.flushDirty(player);
        player.displayClientMessage(Component.translatable("error422.msg.advancements_reset"), true);
    }
}
