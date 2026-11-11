package com.example.error422.entity;

import com.example.error422.config.Error422Config;
import com.example.error422.registry.ModItems;
import com.example.error422.world.Error422Manager;
import com.example.error422.world.GlitchManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

/**
 * Error 422: bozuk surumun canlanmis varligi.
 * Once izler, sonra sinyali keser ve avlanir. Dogrudan saldiriyla oldurulemez;
 * Hata Raporu ile baglantisi sonlandirilabilir.
 */
public class Error422Entity extends PathfinderMob {

    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(Error422Entity.class, EntityDataSerializers.STRING);

    @Nullable
    private UUID ownerUUID;
    private int stateTicks;
    private boolean dropsLoot;

    public Error422Entity(EntityType<? extends Error422Entity> type, Level level) {
        super(type, level);
        this.setPersistenceRequired();
        this.setCustomName(Component.literal("§cERROR 422"));
        this.setCustomNameVisible(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.32D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.55D, true));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 64.0F, 1.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, Error422State.STALK.id());
    }

    public Error422State getState() {
        return Error422State.byId(this.entityData.get(DATA_STATE));
    }

    public void setState(Error422State state) {
        this.entityData.set(DATA_STATE, state.id());
        this.stateTicks = 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("ErrorState", this.getState().id());
        tag.putInt("ErrorStateTicks", this.stateTicks);
        tag.putBoolean("DropsLoot", this.dropsLoot);
        if (this.ownerUUID != null) {
            tag.putUUID("ErrorOwner", this.ownerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ErrorState")) {
            this.setState(Error422State.byId(tag.getString("ErrorState")));
        }
        this.stateTicks = tag.getInt("ErrorStateTicks");
        this.dropsLoot = tag.getBoolean("DropsLoot");
        if (tag.hasUUID("ErrorOwner")) {
            this.ownerUUID = tag.getUUID("ErrorOwner");
        }
    }

    @Nullable
    public ServerPlayer getOwner() {
        if (this.ownerUUID == null) {
            return null;
        }
        MinecraftServer server = this.getServer();
        return server == null ? null : server.getPlayerList().getPlayer(this.ownerUUID);
    }

    public void setOwner(ServerPlayer player) {
        this.ownerUUID = player.getUUID();
    }

    public boolean isOwnedBy(Player player) {
        return this.ownerUUID != null && this.ownerUUID.equals(player.getUUID());
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.clientTick();
            return;
        }
        this.serverTick();
    }

    private void clientTick() {
        if (this.random.nextInt(3) == 0) {
            double x = this.getX() + (this.random.nextDouble() - 0.5D) * 0.8D;
            double y = this.getY() + 0.2D + this.random.nextDouble() * 1.9D;
            double z = this.getZ() + (this.random.nextDouble() - 0.5D) * 0.8D;
            this.level().addParticle(this.random.nextBoolean() ? ParticleTypes.PORTAL : ParticleTypes.ELECTRIC_SPARK,
                    x, y, z, 0.0D, 0.02D, 0.0D);
        }
    }

    private void serverTick() {
        if (this.getState() == Error422State.FADE) {
            this.stateTicks++;
            this.tickFade();
            return;
        }
        ServerPlayer owner = this.getOwner();
        if (owner == null || owner.isDeadOrDying() || owner.isSpectator()) {
            this.startFade();
            return;
        }
        if (owner.isCreative()) {
            if (this.tickCount % 20 == 0) {
                owner.sendSystemMessage(Component.translatable("error422.msg.cheats_entity"));
            }
            this.startFade();
            return;
        }
        if (owner.level() != this.level()) {
            ServerLevel target = owner.serverLevel();
            this.teleportTo(target, owner.getX(), owner.getY(), owner.getZ(), Set.of(), 0.0F, 0.0F);
            return;
        }
        if (this.distanceToSqr(owner) > 128.0D * 128.0D) {
            this.teleportNear(owner, 24);
        }
        this.stateTicks++;
        switch (this.getState()) {
            case STALK -> this.tickStalk(owner);
            case GLITCH -> this.tickGlitch(owner);
            case HUNT -> this.tickHunt(owner);
            case FADE -> this.tickFade();
        }
    }

    private void startFade() {
        if (this.getState() != Error422State.FADE) {
            this.setState(Error422State.FADE);
        }
    }

    private void tickStalk(ServerPlayer owner) {
        this.getLookControl().setLookAt(owner, 30.0F, 30.0F);
        double distance = this.distanceToSqr(owner);
        if (distance > 32.0D * 32.0D) {
            this.teleportNear(owner, 20);
        } else if (distance > 20.0D * 20.0D) {
            this.getNavigation().moveTo(owner, 0.3D);
        } else {
            this.getNavigation().stop();
        }
        if (this.stateTicks % 120 == 0 && this.random.nextFloat() < 0.35F) {
            this.teleportNear(owner, 5);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.4F, 0.4F);
        }
        if (this.tickCount % 40 == 0) {
            this.playSound(SoundEvents.ENDERMAN_STARE, 1.2F, 0.55F);
            this.spawnParticles(10);
        }
        if (this.stateTicks > 200 + this.random.nextInt(200)) {
            this.setState(Error422State.GLITCH);
            owner.sendSystemMessage(Component.translatable("error422.msg.signal_lost"));
        }
    }

    private void tickGlitch(ServerPlayer owner) {
        if (this.stateTicks == 1) {
            GlitchManager.scrambleInventory(owner);
        }
        this.getNavigation().stop();
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        this.getLookControl().setLookAt(owner, 30.0F, 30.0F);
        if (this.stateTicks % 10 == 0) {
            this.spawnParticles(18);
            this.playSound(SoundEvents.ENDERMAN_SCREAM, 1.4F, 0.4F + this.random.nextFloat() * 0.4F);
        }
        if (this.stateTicks > 60) {
            this.setState(Error422State.HUNT);
            this.setTarget(owner);
        }
    }

    private void tickHunt(ServerPlayer owner) {
        this.setTarget(owner);
        if (this.stateTicks % 100 == 1) {
            this.playSound(SoundEvents.WARDEN_ROAR, 2.0F, 0.5F);
        }
        if (this.stateTicks % 40 == 0) {
            owner.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0, false, false));
            owner.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 0, false, false));
            owner.sendSystemMessage(Component.translatable("error422.msg.signal_lost"));
        }
        double distance = this.distanceToSqr(owner);
        if (distance > 16.0D * 16.0D && this.random.nextInt(30) == 0) {
            this.teleportBehind(owner);
        }
        if (distance > 48.0D * 48.0D) {
            this.teleportNear(owner, 12);
        }
        if (this.stateTicks > Error422Config.HUNT_DURATION.get()) {
            this.dropsLoot = true;
            this.setState(Error422State.FADE);
            owner.sendSystemMessage(Component.translatable("error422.msg.signal_restored"));
        }
    }

    private void tickFade() {
        this.getNavigation().stop();
        if (this.stateTicks % 4 == 0) {
            this.spawnParticles(8);
        }
        if (this.stateTicks > 40) {
            if (this.dropsLoot) {
                this.dropLoot();
            }
            this.discard();
        }
    }

    public void banish(ServerPlayer player) {
        if (!this.isAlive()) {
            return;
        }
        this.dropsLoot = true;
        this.setState(Error422State.FADE);
        this.playSound(SoundEvents.ENDERMAN_DEATH, 1.8F, 0.5F);
        this.spawnParticles(60);
        player.sendSystemMessage(Component.translatable("error422.msg.banished"));
        Error422Manager.onBanished(player, this);
    }

    private void dropLoot() {
        this.spawnItem(new ItemStack(ModItems.GLITCH_FRAGMENT.get(), 1 + this.random.nextInt(3)));
        if (this.random.nextBoolean()) {
            this.spawnItem(new ItemStack(ModItems.CORRUPTED_DATA.get()));
        }
        if (Error422Config.ENABLE_RARE_DROPS.get()) {
            Item[] rare = {
                    Items.REPEATER, Items.COMPARATOR, Items.COMMAND_BLOCK, Items.BARRIER,
                    Items.STRUCTURE_VOID, Items.SPAWNER, Items.END_PORTAL_FRAME, Items.DEBUG_STICK
            };
            int count = 1 + this.random.nextInt(3);
            for (int i = 0; i < count; i++) {
                this.spawnItem(new ItemStack(rare[this.random.nextInt(rare.length)]));
            }
        }
    }

    private void spawnItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY() + 0.8D, this.getZ(), stack);
        item.setPickUpDelay(10);
        this.level().addFreshEntity(item);
    }

    private void teleportNear(ServerPlayer owner, int distance) {
        ServerLevel level = owner.serverLevel();
        for (int i = 0; i < 16; i++) {
            double angle = this.random.nextDouble() * Math.PI * 2.0D;
            int d = distance + this.random.nextInt(8);
            double x = owner.getX() + Math.cos(angle) * d;
            double z = owner.getZ() + Math.sin(angle) * d;
            double y = owner.getY();
            BlockPos base = BlockPos.containing(x, y, z);
            BlockPos safe = null;
            for (int dy = 4; dy >= -6; dy--) {
                BlockPos candidate = base.offset(0, dy, 0);
                if (level.isEmptyBlock(candidate) && level.isEmptyBlock(candidate.above())) {
                    safe = candidate;
                    break;
                }
            }
            if (safe != null) {
                this.teleportTo(level, safe.getX() + 0.5D, safe.getY(), safe.getZ() + 0.5D,
                        Set.of(), this.random.nextFloat() * 360.0F, 0.0F);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 0.5F);
                return;
            }
        }
    }

    private void teleportBehind(ServerPlayer owner) {
        Vec3 look = owner.getViewVector(1.0F);
        double x = owner.getX() - look.x * 3.0D;
        double z = owner.getZ() - look.z * 3.0D;
        ServerLevel level = owner.serverLevel();
        BlockPos pos = BlockPos.containing(x, owner.getY(), z);
        if (level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above())) {
            this.teleportTo(level, x, owner.getY(), z, Set.of(), owner.getYRot(), 0.0F);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 0.4F);
        }
    }

    private void spawnParticles(int count) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        serverLevel.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1.0D, this.getZ(),
                count, 0.5D, 0.9D, 0.5D, 0.1D);
        serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY() + 1.0D, this.getZ(),
                Math.max(1, count / 2), 0.4D, 0.8D, 0.4D, 0.05D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) {
            return false;
        }
        this.playSound(SoundEvents.ENDERMAN_HURT, 1.2F, 0.4F);
        this.spawnParticles(16);
        if (source.getEntity() instanceof ServerPlayer player && this.isOwnedBy(player)) {
            player.sendSystemMessage(Component.translatable("error422.msg.invulnerable"));
        }
        return false;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public void checkDespawn() {
        // Kalici varlik: kendiliginden despawn olmaz.
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WARDEN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDERMAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WITHER_DEATH;
    }
}
