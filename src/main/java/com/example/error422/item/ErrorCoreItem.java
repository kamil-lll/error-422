package com.example.error422.item;

import com.example.error422.entity.Error422Entity;
import com.example.error422.world.Error422Manager;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Test/icerik uretici icin: kullanildiginda Error 422'yi cagirir.
 */
public class ErrorCoreItem extends Item {

    public ErrorCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }
        Error422Entity existing = Error422Manager.find(serverPlayer);
        if (existing != null) {
            existing.banish(serverPlayer);
        }
        Error422Entity entity = Error422Manager.spawnFor(serverPlayer, true);
        if (entity != null) {
            serverPlayer.sendSystemMessage(Component.translatable("error422.msg.core_active"));
        }
        if (!player.isCreative()) {
            stack.shrink(1);
        }
        return InteractionResultHolder.consume(stack);
    }
}
