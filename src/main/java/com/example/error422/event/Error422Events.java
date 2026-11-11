package com.example.error422.event;

import com.example.error422.Error422Mod;
import com.example.error422.config.Error422Config;
import com.example.error422.entity.Error422Entity;
import com.example.error422.world.AdvancementReset;
import com.example.error422.world.Error422Manager;
import com.example.error422.world.GlitchManager;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Error422Mod.MODID)
public final class Error422Events {

    private Error422Events() {
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Error422Manager.onLogin(player);
            if (Error422Config.ENABLE_ADVANCEMENT_RESET.get()
                    && player.getRandom().nextFloat() < 0.2F) {
                AdvancementReset.reset(player);
            }
            MinecraftServer server = player.getServer();
            if (server != null && Error422Config.FORCE_SURVIVAL.get()
                    && server.getWorldData().isAllowCommands()) {
                player.sendSystemMessage(Component.translatable("error422.msg.cheats"));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.player instanceof ServerPlayer player) {
            if (Error422Config.FORCE_SURVIVAL.get() && (player.isCreative() || player.isSpectator())) {
                player.setGameMode(GameType.SURVIVAL);
                player.sendSystemMessage(Component.translatable("error422.msg.cheats_survival"));
            }
            Error422Manager.onTick(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Error422Manager.onRespawn(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            event.getEntity().getPersistentData().merge(event.getOriginal().getPersistentData());
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!Error422Config.ENABLE_BLOCK_GLITCHES.get()) {
            return;
        }
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        if (player.isCreative() || player.getRandom().nextFloat() > 0.05F) {
            return;
        }
        BlockPos pos = event.getPos();
        ServerLevel level = player.serverLevel();
        boolean lava = Error422Config.ENABLE_LAVA_GLITCH.get() && player.getRandom().nextFloat() < 0.1F;
        BlockState replacement = lava ? Blocks.LAVA.defaultBlockState() : Blocks.WATER.defaultBlockState();
        MinecraftServer server = player.getServer();
        if (server != null) {
            server.execute(() -> level.setBlockAndUpdate(pos, replacement));
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        Level level = event.getLevel();
        ItemStack stack = event.getItemStack();

        if (Error422Config.DISABLE_CONTROLS_DURING_HUNT.get() && Error422Manager.isHuntingNear(player)) {
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.setCanceled(true);
            return;
        }

        // Ekinleri her blogun uzerine dikebilme (creepypasta detayi).
        if (Error422Config.ENABLE_ANYWHERE_CROPS.get()
                && event.getHitVec().getDirection() == Direction.UP
                && stack.getItem() instanceof ItemNameBlockItem seed) {
            BlockPos cropPos = event.getPos().above();
            if (level.getBlockState(cropPos).canBeReplaced()) {
                Block crop = seed.getBlock();
                level.setBlock(cropPos, crop.defaultBlockState(), 2);
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
                player.swing(event.getHand());
                event.setUseBlock(Event.Result.DENY);
                event.setUseItem(Event.Result.DENY);
                event.setCanceled(true);
                return;
            }
        }

        // Overworld'de yataklarin nadiren patlamasi.
        if (Error422Config.ENABLE_BED_EXPLOSION.get()
                && level.dimension() == Level.OVERWORLD
                && level.getBlockState(event.getPos()).is(BlockTags.BEDS)
                && player.getRandom().nextFloat() < 0.02F) {
            BlockPos pos = event.getPos();
            level.explode(player, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    1.5F, Level.ExplosionInteraction.BLOCK);
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onEntityPlace(BlockEvent.EntityPlaceEvent event) {
        if (!Error422Config.ENABLE_PLACEMENT_GLITCH.get()) {
            return;
        }
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (player.isCreative() || player.getRandom().nextFloat() > 0.05F) {
            return;
        }
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        BlockState[] replacements = {
                Blocks.STONE.defaultBlockState(),
                Blocks.COBBLESTONE.defaultBlockState(),
                Blocks.DIRT.defaultBlockState(),
                Blocks.SAND.defaultBlockState(),
                Blocks.GRAVEL.defaultBlockState(),
                Blocks.GLASS.defaultBlockState(),
                Blocks.REDSTONE_BLOCK.defaultBlockState(),
                Blocks.OBSIDIAN.defaultBlockState()
        };
        event.setCanceled(true);
        level.setBlockAndUpdate(event.getPos(),
                replacements[player.getRandom().nextInt(replacements.length)]);
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (Error422Config.DISABLE_CONTROLS_DURING_HUNT.get()
                && event.getEntity() instanceof ServerPlayer player
                && Error422Manager.isHuntingNear(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (Error422Config.DISABLE_CONTROLS_DURING_HUNT.get()
                && event.getEntity() instanceof ServerPlayer player
                && Error422Manager.isHuntingNear(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (Error422Config.DISABLE_CONTROLS_DURING_HUNT.get()
                && event.getEntity() instanceof ServerPlayer player
                && Error422Manager.isHuntingNear(player)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!Error422Config.DISABLE_VOID_DAMAGE.get()) {
            return;
        }
        if (event.getEntity() instanceof Player player
                && player.getY() < player.level().getMinBuildHeight() - 64.0D) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack crafted = event.getCrafting();
        if (crafted.isEmpty()) {
            return;
        }
        boolean blocked = Error422Config.ENABLE_BROKEN_RECIPES.get()
                && (crafted.is(ItemTags.FENCES)
                || crafted.is(Items.GOLDEN_APPLE)
                || crafted.is(Items.ENCHANTED_GOLDEN_APPLE));
        boolean random = Error422Config.ENABLE_RANDOM_CRAFTING.get()
                && player.getRandom().nextFloat() < 0.15F;
        if (!blocked && !random) {
            return;
        }
        ItemStack copy = crafted.copy();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }
        server.execute(() -> {
            player.getInventory().removeItem(copy);
            if (random && !blocked) {
                ItemStack result = new ItemStack(randomCraftItem(player), copy.getCount());
                if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }
                player.displayClientMessage(Component.translatable("error422.msg.crafting_error"), true);
            } else {
                player.displayClientMessage(Component.translatable("error422.msg.recipe_corrupted"), true);
            }
        });
    }

    private static Item randomCraftItem(ServerPlayer player) {
        Item[] items = {
                Items.DIRT, Items.STICK, Items.BREAD, Items.APPLE, Items.COAL,
                Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD,
                Items.REDSTONE, Items.LAPIS_LAZULI, Items.FLINT, Items.STRING,
                Items.FEATHER, Items.ROTTEN_FLESH, Items.BONE, Items.GLASS,
                Items.SAND, Items.OAK_LOG, Items.TORCH, Items.ARROW, Items.GUNPOWDER,
                Items.ENDER_PEARL, Items.SLIME_BALL, Items.HONEYCOMB
        };
        return items[player.getRandom().nextInt(items.length)];
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (Error422Config.DISABLE_FALL_DAMAGE.get() && event.getEntity() instanceof Player) {
            event.setDamageMultiplier(0.0F);
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("error422")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("spawn").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Error422Entity entity = Error422Manager.spawnFor(player, true);
                    context.getSource().sendSuccess(() -> Component.translatable(
                            entity == null ? "error422.cmd.spawn.fail" : "error422.cmd.spawn.ok"), false);
                    return 1;
                }))
                .then(Commands.literal("glitch").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    GlitchManager.trigger(player);
                    context.getSource().sendSuccess(() -> Component.translatable("error422.cmd.glitch"), false);
                    return 1;
                }))
                .then(Commands.literal("banish").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Error422Entity entity = Error422Manager.find(player);
                    if (entity != null) {
                        entity.banish(player);
                    }
                    context.getSource().sendSuccess(() -> Component.translatable(
                            entity == null ? "error422.cmd.banish.none" : "error422.cmd.banish.ok"), false);
                    return 1;
                }))
                .then(Commands.literal("flora").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    GlitchManager.floatingFlora(player);
                    context.getSource().sendSuccess(() -> Component.translatable("error422.cmd.flora"), false);
                    return 1;
                }))
                .then(Commands.literal("status").executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Error422Entity entity = Error422Manager.find(player);
                    context.getSource().sendSuccess(() -> {
                        Component state = entity == null
                                ? Component.translatable("error422.cmd.status.idle")
                                : Component.literal(entity.getState().id());
                        return Component.translatable("error422.cmd.status", state);
                    }, false);
                    return 1;
                })));
    }
}
