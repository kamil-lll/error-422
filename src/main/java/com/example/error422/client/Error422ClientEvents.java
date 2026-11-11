package com.example.error422.client;

import com.example.error422.Error422Mod;
import com.example.error422.config.Error422Config;
import com.example.error422.entity.Error422Entity;
import com.example.error422.entity.Error422State;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState.SelectedGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Istemci tarafi korku efektleri: sahte cokme ekrani, kamera sarsintisi,
 * FOV nabzi ve bozulan esya tooltip'leri.
 */
@Mod.EventBusSubscriber(modid = Error422Mod.MODID, value = Dist.CLIENT)
public final class Error422ClientEvents {

    @Nullable
    private static UUID lastCrashEntity;
    private static long lastCrashTime;
    @Nullable
    private static UUID lastVanishEntity;
    private static long lastVanishTime;
    @Nullable
    private static GlitchAmbienceSound glitchSound;
    private static boolean anaglyphActive;
    private static int xrayTicks;
    private static int xrayCooldown;
    private static boolean warningShown;
    private static int versionTicks = -1;
    private static boolean versionMessageShown;

    private static void handleAmbience(Minecraft minecraft) {
        if (minecraft.screen instanceof Error422WarningScreen) {
            if (glitchSound != null && minecraft.getSoundManager().isActive(glitchSound)) {
                minecraft.getSoundManager().stop(glitchSound);
            }
            glitchSound = null;
            return;
        }
        if (!Error422Config.REPLACE_MUSIC.get()) {
            if (glitchSound != null && minecraft.getSoundManager().isActive(glitchSound)) {
                minecraft.getSoundManager().stop(glitchSound);
            }
            glitchSound = null;
            return;
        }
        // Muzik sesi 0'a cekilince Minecraft stream'i birakiyor; geri acilinca yeniden baslat.
        if (minecraft.options.getSoundSourceVolume(SoundSource.MUSIC) <= 0.0F) {
            if (glitchSound != null && minecraft.getSoundManager().isActive(glitchSound)) {
                minecraft.getSoundManager().stop(glitchSound);
            }
            glitchSound = null;
            return;
        }
        if (glitchSound == null || !minecraft.getSoundManager().isActive(glitchSound)) {
            glitchSound = new GlitchAmbienceSound();
            minecraft.getSoundManager().play(glitchSound);
            minecraft.getMusicManager().stopPlaying();
        }
    }

    private static void handleVersionGlitch(Minecraft minecraft) {
        if (!Error422Config.ENABLE_VERSION_GLITCH.get()) {
            return;
        }
        if (versionTicks < 0) {
            versionTicks = 1200 + ThreadLocalRandom.current().nextInt(2400);
            Error422ClientState.headlessPlayer = true;
            Error422ClientState.versionString = "1.2.1";
        } else if (--versionTicks <= 0) {
            versionTicks = 1200 + ThreadLocalRandom.current().nextInt(2400);
            Error422ClientState.headlessPlayer = !Error422ClientState.headlessPlayer;
            Error422ClientState.versionString = Error422ClientState.headlessPlayer ? "1.2.1" : "1.9.1";
            if (minecraft.player != null) {
                minecraft.player.displayClientMessage(Component.literal("§7Minecraft " + Error422ClientState.versionString), true);
            }
        }
        if (minecraft.player != null && !versionMessageShown) {
            versionMessageShown = true;
            minecraft.player.displayClientMessage(Component.literal("§7Minecraft " + Error422ClientState.versionString), true);
        }
    }

    private static void disableAnaglyph(Minecraft minecraft) {
        if (anaglyphActive) {
            minecraft.gameRenderer.shutdownEffect();
            anaglyphActive = false;
            Error422ClientState.anaglyph = false;
        }
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if (Error422Config.REPLACE_MUSIC.get()
                && event.getSound() != null
                && event.getSound().getSource() == SoundSource.MUSIC
                && !(event.getSound() instanceof GlitchAmbienceSound)) {
            event.setResult(Event.Result.DENY);
        }
    }

    private Error422ClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (Error422ClientState.inventoryGlitchTicks > 0) {
            Error422ClientState.inventoryGlitchTicks--;
        }
        Minecraft minecraft = Minecraft.getInstance();
        if (!warningShown
                && Error422Config.SHOW_WARNING_SCREEN.get()
                && minecraft.screen instanceof TitleScreen) {
            warningShown = true;
            minecraft.setScreen(new Error422WarningScreen());
            Error422ClientState.xray = false;
            return;
        }
        handleVersionGlitch(minecraft);
        handleAmbience(minecraft);
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            disableAnaglyph(minecraft);
            Error422ClientState.xray = false;
            return;
        }
        if (minecraft.screen != null) {
            Error422ClientState.xray = false;
            return;
        }
        Error422Entity entity = findNearest(player, 64.0D);
        if (entity == null) {
            disableAnaglyph(minecraft);
            Error422ClientState.xray = false;
            return;
        }
        boolean shouldAnaglyph = Error422Config.ENABLE_ANAGLYPH.get()
                && entity.distanceToSqr(player) <= 48.0D * 48.0D
                && entity.getState() != Error422State.FADE;
        if (shouldAnaglyph != anaglyphActive) {
            if (shouldAnaglyph) {
                minecraft.gameRenderer.loadEffect(
                        ResourceLocation.fromNamespaceAndPath(Error422Mod.MODID, "shaders/post/anaglyph.json"));
                anaglyphActive = true;
                Error422ClientState.anaglyph = true;
                player.displayClientMessage(Component.translatable("error422.msg.anaglyph"), true);
            } else {
                minecraft.gameRenderer.shutdownEffect();
                anaglyphActive = false;
                Error422ClientState.anaglyph = false;
            }
        }
        Error422ClientState.xray = false;
        if (Error422Config.ENABLE_XRAY_FLASH.get()
                && entity.getState() == Error422State.HUNT
                && entity.distanceToSqr(player) <= 32.0D * 32.0D) {
            if (xrayCooldown <= 0) {
                xrayTicks = 12 + player.getRandom().nextInt(14);
                xrayCooldown = 30 + player.getRandom().nextInt(40);
                player.displayClientMessage(Component.translatable("error422.msg.xray"), true);
                minecraft.level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ENDERMAN_SCREAM, SoundSource.AMBIENT, 0.7F, 0.5F, false);
            }
        }
        if (xrayCooldown > 0) {
            xrayCooldown--;
        }
        if (xrayTicks > 0) {
            xrayTicks--;
            Error422ClientState.xray = true;
        }
        long now = System.currentTimeMillis();
        if (entity.getState() == Error422State.GLITCH && Error422Config.ENABLE_CRASH_SCREEN.get()) {
            if (entity.getUUID().equals(lastCrashEntity) && now - lastCrashTime < 5000L) {
                return;
            }
            lastCrashEntity = entity.getUUID();
            lastCrashTime = now;
            minecraft.setScreen(new Error422CrashScreen());
            minecraft.level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_SPAWN, SoundSource.AMBIENT, 1.0F, 0.5F, false);
            return;
        }
        if (entity.getState() == Error422State.FADE && Error422Config.ENABLE_VANISH_OVERLAY.get()) {
            if (entity.getUUID().equals(lastVanishEntity) && now - lastVanishTime < 5000L) {
                return;
            }
            lastVanishEntity = entity.getUUID();
            lastVanishTime = now;
            minecraft.setScreen(new Error422VanishScreen());
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        if (!Error422Config.ENABLE_HEADLESS_PLAYER.get() || !Error422ClientState.headlessPlayer) {
            return;
        }
        var model = event.getRenderer().getModel();
        model.head.visible = false;
        model.hat.visible = false;
    }

    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (!Error422Config.ENABLE_HEADLESS_PLAYER.get() || !Error422ClientState.headlessPlayer) {
            return;
        }
        var model = event.getRenderer().getModel();
        model.head.visible = true;
        model.hat.visible = true;
    }

    @SubscribeEvent
    public static void onScreenOpening(ScreenEvent.Opening event) {
        if (Error422Config.ENABLE_INVENTORY_LOAD_GLITCH.get()
                && event.getNewScreen() instanceof InventoryScreen) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.getRandom().nextFloat() < 0.2F) {
                Error422ClientState.inventoryGlitchTicks = 80 + player.getRandom().nextInt(80);
            }
        }
        if (!Error422Config.ENABLE_MENU_LOCK.get()) {
            return;
        }
        if (!(event.getNewScreen() instanceof PauseScreen)) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Error422Entity entity = findNearest(player, 48.0D);
        if (entity == null || (entity.getState() != Error422State.HUNT && entity.getState() != Error422State.GLITCH)) {
            return;
        }
        event.setNewScreen(new Error422CrashScreen());
    }

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CreateWorldScreen createWorldScreen
                && Error422Config.FORCE_SURVIVAL.get()) {
            createWorldScreen.getUiState().setGameMode(SelectedGameMode.SURVIVAL);
            createWorldScreen.getUiState().setAllowCommands(false);
            return;
        }
        if (event.getScreen() instanceof PauseScreen) {
            if (Error422Config.PERMANENT_QUIT_LOCK.get()) {
                for (GuiEventListener listener : event.getListenersList()) {
                    if (listener instanceof Button button) {
                        Component message = button.getMessage();
                        if (message.getContents() instanceof TranslatableContents t
                                && (t.getKey().equals("menu.returnToMenu") || t.getKey().equals("menu.disconnect"))) {
                            button.active = false;
                            button.setMessage(Component.translatable("error422.menu.save_quit_locked"));
                        }
                    }
                }
            }
            return;
        }
        if (!(event.getScreen() instanceof TitleScreen)) {
            return;
        }
        for (GuiEventListener listener : event.getListenersList()) {
            if (!(listener instanceof Button button)) {
                continue;
            }
            Component message = button.getMessage();
            if (!(message.getContents() instanceof TranslatableContents translatable)) {
                continue;
            }
            String key = translatable.getKey();
            if (Error422Config.DISABLE_MULTIPLAYER_BUTTON.get() && key.equals("menu.multiplayer")) {
                button.active = false;
            }
            if (!Error422Config.CORRUPT_MENU_BUTTONS.get()) {
                continue;
            }
            switch (key) {
                case "menu.singleplayer" -> button.setMessage(Component.translatable("error422.menu.singleplayer"));
                case "menu.multiplayer" -> button.setMessage(Component.translatable("error422.menu.multiplayer"));
                case "menu.online" -> button.setMessage(Component.translatable("error422.menu.realms"));
                case "menu.options" -> button.setMessage(Component.translatable("error422.menu.options"));
                case "menu.quit" -> button.setMessage(Component.translatable("error422.menu.quit"));
                default -> {
                }
            }
            if (Error422Config.PERMANENT_QUIT_LOCK.get() && key.equals("menu.quit")) {
                button.active = false;
                button.setMessage(Component.translatable("error422.menu.quit_locked"));
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        if (!Error422Config.DISABLE_CONTROLS_DURING_HUNT.get()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Error422Entity entity = findNearest(player, 32.0D);
        if (entity != null && entity.getState() == Error422State.HUNT) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Error422Entity entity = findNearest(player, 32.0D);
        if (entity == null || entity.getState() != Error422State.HUNT) {
            return;
        }
        RandomSource random = player.getRandom();
        event.setYaw(event.getYaw() + (random.nextFloat() - 0.5F) * 3.0F);
        event.setPitch(event.getPitch() + (random.nextFloat() - 0.5F) * 2.0F);
        event.setRoll(event.getRoll() + (random.nextFloat() - 0.5F) * 2.0F);
    }

    @SubscribeEvent
    public static void onFov(ViewportEvent.ComputeFov event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }
        Error422Entity entity = findNearest(player, 24.0D);
        if (entity == null || entity.getState() != Error422State.HUNT) {
            return;
        }
        event.setFOV(event.getFOV() + 6.0D);
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!Error422Config.ENABLE_TOOLTIP_GLITCH.get()) {
            return;
        }
        Player player = event.getEntity();
        if (player == null) {
            return;
        }
        Error422Entity entity = findNearest(player, 32.0D);
        if (entity == null || event.getToolTip().isEmpty()) {
            return;
        }
        String name = event.getItemStack().getHoverName().getString();
        event.getToolTip().set(0, Component.literal("§k" + scramble(name, player.getRandom())));
    }

    @SubscribeEvent
    public static void onDebugText(CustomizeGuiOverlayEvent.DebugText event) {
        if (Error422Config.ENABLE_VERSION_GLITCH.get()) {
            List<String> versionText = event.getText();
            try {
                for (int i = 0; i < versionText.size(); i++) {
                    String line = versionText.get(i);
                    if (line.contains("Minecraft")) {
                        versionText.set(i, "Minecraft " + Error422ClientState.versionString);
                    }
                }
            } catch (UnsupportedOperationException ignored) {
            }
        }
        if (!Error422Config.ENABLE_DEBUG_SCRAMBLE.get()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || findNearest(player, 48.0D) == null) {
            return;
        }
        List<String> text = event.getText();
        RandomSource random = player.getRandom();
        try {
            for (int i = 0; i < text.size(); i++) {
                text.set(i, scramble(text.get(i), random));
            }
        } catch (UnsupportedOperationException ignored) {
            // Bazi surumlerde liste degistirilemez olabilir.
        }
    }

    @Nullable
    private static Error422Entity findNearest(Player player, double range) {
        List<Error422Entity> entities = player.level().getEntitiesOfClass(Error422Entity.class,
                player.getBoundingBox().inflate(range), Entity::isAlive);
        Error422Entity nearest = null;
        double best = Double.MAX_VALUE;
        for (Error422Entity entity : entities) {
            double distance = entity.distanceToSqr(player);
            if (distance < best) {
                best = distance;
                nearest = entity;
            }
        }
        return nearest;
    }

    private static String scramble(String input, RandomSource random) {
        StringBuilder builder = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ') {
                builder.append(' ');
            } else {
                builder.append((char) ('!' + random.nextInt(90)));
            }
        }
        return builder.toString();
    }
}
