package com.example.error422.world;

import com.example.error422.config.Error422Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Rastgele sistem hatalari: sohbet dokumleri, yanlis sesler, efektler,
 * zaman atlamalari, blok bozulmalari ve daha fazlasi.
 */
public final class GlitchManager {

    private static final String TAG_NEXT = "error422:next_glitch";

    private static final SoundEvent[] SOUNDS = {
            SoundEvents.ENDERMAN_STARE,
            SoundEvents.ENDERMAN_SCREAM,
            SoundEvents.WARDEN_AMBIENT,
            SoundEvents.GHAST_SCREAM,
            SoundEvents.ELDER_GUARDIAN_CURSE,
            SoundEvents.SCULK_SHRIEKER_SHRIEK,
            SoundEvents.WITHER_AMBIENT
    };

    private GlitchManager() {
    }

    public static void tick(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        long now = player.level().getGameTime();
        if (now < data.getLong(TAG_NEXT)) {
            return;
        }
        trigger(player);
        data.putLong(TAG_NEXT, now + Error422Config.GLITCH_INTERVAL.get()
                + player.getRandom().nextInt(600));
    }

    public static void trigger(ServerPlayer player) {
        if (player.isCreative() || player.isSpectator()) {
            return;
        }
        int roll = player.getRandom().nextInt(100);
        if (roll < 15) {
            errorDump(player);
        } else if (roll < 28) {
            wrongSound(player);
        } else if (roll < 40) {
            potion(player);
        } else if (roll < 50) {
            timeSkip(player);
        } else if (roll < 58) {
            push(player);
        } else if (roll < 65 && Error422Config.ENABLE_BLOCK_GLITCHES.get()) {
            blockSwap(player);
        } else if (roll < 71 && Error422Config.ENABLE_BLOCK_GLITCHES.get()) {
            waterToLava(player);
        } else if (roll < 78 && Error422Config.ENABLE_FLOATING_FLORA.get()) {
            floatingFlora(player);
        } else if (roll < 84 && Error422Config.ENABLE_FIRE_GLITCH.get()) {
            fireAround(player);
        } else if (roll < 89 && Error422Config.ENABLE_CREEPER_GLITCH.get()) {
            creeper(player);
        } else if (roll < 92) {
            resetAdvancements(player);
        } else if (roll < 95 && Error422Config.ENABLE_FAKE_WORLD_CORRUPTION.get()) {
            fakeWorldCorruption(player);
        } else if (roll < 97 && Error422Config.ENABLE_HOTBAR_GLITCH.get()) {
            hotbarSwap(player);
        } else if (Error422Config.ENABLE_LIGHTNING.get()) {
            lightning(player);
        } else {
            errorDump(player);
        }
    }

    private static void errorDump(ServerPlayer player) {
        String[] lines = {
                "§c[ERROR 422] §7java.lang.NullPointerException: null",
                "§8    at net.minecraft.client.Minecraft.run(Unknown Source)",
                "§8    at §k" + "X".repeat(8 + player.getRandom().nextInt(24)),
                "§c§kERRORERRORERRORERROR§r §7// SIGNAL LOST"
        };
        int count = 2 + player.getRandom().nextInt(2);
        for (int i = 0; i < count; i++) {
            player.sendSystemMessage(Component.literal(lines[player.getRandom().nextInt(lines.length)]));
        }
    }

    private static void wrongSound(ServerPlayer player) {
        SoundEvent sound = SOUNDS[player.getRandom().nextInt(SOUNDS.length)];
        player.playNotifySound(sound, SoundSource.AMBIENT, 1.0F,
                0.4F + player.getRandom().nextFloat() * 0.6F);
    }

    private static void potion(ServerPlayer player) {
        int roll = player.getRandom().nextInt(5);
        switch (roll) {
            case 0 -> player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 120, 0, false, false));
            case 1 -> player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, false));
            case 2 -> player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, false, false));
            case 3 -> player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
            default -> player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40, 0, false, false));
        }
    }

    private static void timeSkip(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        level.setDayTime(level.getDayTime() + 1000L + player.getRandom().nextInt(5000));
    }

    private static void push(ServerPlayer player) {
        player.push((player.getRandom().nextDouble() - 0.5D) * 0.7D, 0.35D,
                (player.getRandom().nextDouble() - 0.5D) * 0.7D);
    }

    /**
     * Creepypasta'daki ucan/yeralti kaktusleri, niluferler ve olu calilar.
     * Bloklar komsu guncellemesi olmadan yerlestirilir; boylece havada kalirlar.
     */
    public static void floatingFlora(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        int clusters = 2 + player.getRandom().nextInt(3);
        for (int i = 0; i < clusters; i++) {
            BlockPos pos = player.blockPosition().offset(
                    player.getRandom().nextInt(33) - 16,
                    player.getRandom().nextInt(21) - 10,
                    player.getRandom().nextInt(33) - 16);
            int type = player.getRandom().nextInt(3);
            if (type == 0) {
                int height = 3 + player.getRandom().nextInt(2);
                for (int y = 0; y < height; y++) {
                    level.setBlock(pos.above(y), Blocks.CACTUS.defaultBlockState(), 2);
                }
            } else if (type == 1) {
                level.setBlock(pos, Blocks.LILY_PAD.defaultBlockState(), 2);
            } else {
                level.setBlock(pos, Blocks.DEAD_BUSH.defaultBlockState(), 2);
            }
        }
    }

    /**
     * Oyuncunun etrafinda rastgele yangin cikarir.
     */
    private static void fireAround(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        for (int i = 0; i < 6; i++) {
            BlockPos pos = player.blockPosition().offset(
                    player.getRandom().nextInt(13) - 6,
                    player.getRandom().nextInt(5) - 1,
                    player.getRandom().nextInt(13) - 6);
            BlockPos below = pos.below();
            if (level.isEmptyBlock(pos) && level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) {
                level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                return;
            }
        }
    }

    /**
     * Anlik creeper glitch'i: yakinina bir creeper cagirip atesler.
     */
    private static void creeper(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Creeper creeper = EntityType.CREEPER.create(level);
        if (creeper == null) {
            return;
        }
        BlockPos pos = player.blockPosition().offset(
                player.getRandom().nextInt(11) - 5, 0, player.getRandom().nextInt(11) - 5);
        creeper.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
        level.addFreshEntity(creeper);
        creeper.ignite();
        player.playNotifySound(SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 1.0F, 1.0F);
    }

    /**
     * Glitch aninda envanterin 36 ana slotunu rastgele karistirir.
     */
    public static void scrambleInventory(ServerPlayer player) {
        if (!Error422Config.ENABLE_INVENTORY_SCRAMBLE.get()) {
            return;
        }
        Inventory inventory = player.getInventory();
        List<ItemStack> stacks = new ArrayList<>(36);
        for (int i = 0; i < 36; i++) {
            stacks.add(inventory.getItem(i));
        }
        Collections.shuffle(stacks, new Random(player.getRandom().nextLong()));
        for (int i = 0; i < 36; i++) {
            inventory.setItem(i, stacks.get(i));
        }
        player.sendSystemMessage(Component.translatable("error422.msg.inventory_corrupted"));
    }

    private static void blockSwap(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
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
        for (int i = 0; i < 10; i++) {
            BlockPos pos = player.blockPosition().offset(
                    player.getRandom().nextInt(13) - 6,
                    player.getRandom().nextInt(7) - 3,
                    player.getRandom().nextInt(13) - 6);
            BlockState state = level.getBlockState(pos);
            if (state.isAir() || state.hasBlockEntity() || state.getDestroySpeed(level, pos) < 0.0F) {
                continue;
            }
            level.setBlockAndUpdate(pos, replacements[player.getRandom().nextInt(replacements.length)]);
            return;
        }
    }

    private static void waterToLava(ServerPlayer player) {
        if (!Error422Config.ENABLE_LAVA_GLITCH.get()) {
            return;
        }
        ServerLevel level = player.serverLevel();
        for (int i = 0; i < 12; i++) {
            BlockPos pos = player.blockPosition().offset(
                    player.getRandom().nextInt(17) - 8,
                    player.getRandom().nextInt(7) - 3,
                    player.getRandom().nextInt(17) - 8);
            if (level.getBlockState(pos).is(Blocks.WATER)) {
                level.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
                return;
            }
        }
    }

    private static void lightning(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
        if (bolt != null) {
            BlockPos pos = player.blockPosition().offset(
                    player.getRandom().nextInt(9) - 4, 0, player.getRandom().nextInt(9) - 4);
            bolt.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(bolt);
        }
    }

    private static void resetAdvancements(ServerPlayer player) {
        if (Error422Config.ENABLE_ADVANCEMENT_RESET.get()) {
            AdvancementReset.reset(player);
        }
    }

    /**
     * Gercek dosya bozulmasi yapmadan "dunya verisi bozuldu" hissi.
     */
    private static void fakeWorldCorruption(ServerPlayer player) {
        player.sendSystemMessage(Component.translatable("error422.msg.world_corrupted"));
        ServerLevel level = player.serverLevel();
        level.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1.0D, player.getZ(),
                40, 1.2D, 1.0D, 1.2D, 0.02D);
        level.playSound(null, player.blockPosition(), SoundEvents.WITHER_BREAK_BLOCK,
                SoundSource.AMBIENT, 1.0F, 0.5F);
    }

    private static void hotbarSwap(ServerPlayer player) {
        int a = player.getRandom().nextInt(9);
        int b = player.getRandom().nextInt(9);
        if (a == b) {
            return;
        }
        ItemStack first = player.getInventory().getItem(a);
        player.getInventory().setItem(a, player.getInventory().getItem(b));
        player.getInventory().setItem(b, first);
    }
}
