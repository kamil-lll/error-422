package com.example.error422.world;

import com.example.error422.config.Error422Config;
import com.example.error422.entity.Error422Entity;
import com.example.error422.entity.Error422State;
import com.example.error422.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class Error422Manager {

    private static final String TAG_ENTITY = "error422:entity";
    private static final String TAG_NEXT_SPAWN = "error422:next_spawn";
    private static final String TAG_NEXT_GLITCH = "error422:next_glitch";
    private static final String TAG_INTRO = "error422:intro";

    private Error422Manager() {
    }

    public static void onLogin(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        long now = player.level().getGameTime();
        if (!data.getBoolean(TAG_INTRO)) {
            data.putBoolean(TAG_INTRO, true);
            player.sendSystemMessage(Component.translatable("error422.msg.intro"));
        }
        if (!data.contains(TAG_NEXT_SPAWN)) {
            data.putLong(TAG_NEXT_SPAWN, now + Error422Config.SPAWN_DELAY.get());
        }
        if (!data.contains(TAG_NEXT_GLITCH)) {
            data.putLong(TAG_NEXT_GLITCH, now + 600L);
        }
    }

    public static void onTick(ServerPlayer player) {
        if (player.level().getGameTime() % 20L != 0L) {
            return;
        }
        if (Error422Config.ENABLE_ENTITY.get()) {
            tickSpawn(player);
        }
        if (Error422Config.ENABLE_GLITCHES.get()) {
            GlitchManager.tick(player);
        }
    }

    private static void tickSpawn(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        long now = player.level().getGameTime();
        if (now < data.getLong(TAG_NEXT_SPAWN)) {
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            data.putLong(TAG_NEXT_SPAWN, now + 1200L);
            return;
        }
        Error422Entity existing = find(player);
        if (existing != null) {
            data.putLong(TAG_NEXT_SPAWN, now + 2400L);
            return;
        }
        Error422Entity entity = spawnFor(player, false);
        if (entity != null) {
            data.putLong(TAG_NEXT_SPAWN, now + Error422Config.SPAWN_INTERVAL.get()
                    + player.getRandom().nextInt(6000));
        }
    }

    @Nullable
    public static Error422Entity spawnFor(ServerPlayer player, boolean close) {
        ServerLevel level = player.serverLevel();
        Error422Entity entity = ModEntities.ERROR_422.get().create(level);
        if (entity == null) {
            return null;
        }
        double angle = player.getRandom().nextDouble() * Math.PI * 2.0D;
        int distance = close ? 6 : 20 + player.getRandom().nextInt(16);
        double x = player.getX() + Math.cos(angle) * distance;
        double z = player.getZ() + Math.sin(angle) * distance;
        double y = player.getY();
        BlockPos base = BlockPos.containing(x, y, z);
        BlockPos safe = null;
        for (int dy = 4; dy >= -6; dy--) {
            BlockPos candidate = base.offset(0, dy, 0);
            if (level.isEmptyBlock(candidate) && level.isEmptyBlock(candidate.above())) {
                safe = candidate;
                break;
            }
        }
        if (safe == null) {
            safe = base;
        }
        entity.moveTo(safe.getX() + 0.5D, safe.getY(), safe.getZ() + 0.5D,
                player.getYRot() + 180.0F, 0.0F);
        entity.setOwner(player);
        entity.setPersistenceRequired();

        AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(Error422Config.ENTITY_HEALTH.get());
        }
        entity.setHealth((float) (double) Error422Config.ENTITY_HEALTH.get());
        AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.setBaseValue(Error422Config.ENTITY_DAMAGE.get());
        }

        level.addFreshEntity(entity);
        player.getPersistentData().putUUID(TAG_ENTITY, entity.getUUID());
        player.playNotifySound(SoundEvents.ENDERMAN_STARE, SoundSource.AMBIENT, 1.0F, 0.4F);
        player.sendSystemMessage(Component.translatable("error422.msg.spawn"));
        return entity;
    }

    @Nullable
    public static Error422Entity find(ServerPlayer player) {
        CompoundTag data = player.getPersistentData();
        if (!data.hasUUID(TAG_ENTITY)) {
            return null;
        }
        UUID uuid = data.getUUID(TAG_ENTITY);
        MinecraftServer server = player.getServer();
        if (server == null) {
            return null;
        }
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity instanceof Error422Entity error && error.isAlive()) {
                return error;
            }
        }
        data.remove(TAG_ENTITY);
        return null;
    }

    public static boolean isHuntingNear(ServerPlayer player) {
        Error422Entity entity = find(player);
        return entity != null
                && entity.getState() == Error422State.HUNT
                && entity.distanceToSqr(player) <= 32.0D * 32.0D;
    }

    public static void onBanished(ServerPlayer player, Error422Entity entity) {
        player.getPersistentData().remove(TAG_ENTITY);
        player.getPersistentData().putLong(TAG_NEXT_SPAWN,
                player.level().getGameTime() + Error422Config.SPAWN_INTERVAL.get());
    }

    public static void onRespawn(ServerPlayer player) {
        Error422Entity entity = find(player);
        if (entity != null) {
            entity.discard();
        }
        player.getPersistentData().remove(TAG_ENTITY);
        player.getPersistentData().putLong(TAG_NEXT_SPAWN,
                player.level().getGameTime() + 6000L);
    }
}
