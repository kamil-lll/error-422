package com.example.error422.item;

import com.example.error422.entity.Error422Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Error 422'nin baglantisini kesen rapor. Varlik yakinda degilse sahte hata verir.
 */
public class ErrorReportItem extends Item {

    public ErrorReportItem(Properties properties) {
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
        List<Error422Entity> entities = level.getEntitiesOfClass(Error422Entity.class,
                player.getBoundingBox().inflate(24.0D), Entity::isAlive);
        if (entities.isEmpty()) {
            player.sendSystemMessage(Component.translatable("error422.msg.report_empty"));
            player.getCooldowns().addCooldown(this, 60);
            return InteractionResultHolder.fail(stack);
        }
        entities.get(0).banish(serverPlayer);
        player.getCooldowns().addCooldown(this, 100);
        return InteractionResultHolder.consume(stack);
    }
}
