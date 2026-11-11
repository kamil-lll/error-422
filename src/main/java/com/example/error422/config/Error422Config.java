package com.example.error422.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Error422Config {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.BooleanValue ENABLE_ENTITY;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GLITCHES;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BLOCK_GLITCHES;
    public static final ForgeConfigSpec.BooleanValue ENABLE_LAVA_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_LIGHTNING;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HOTBAR_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_TOOLTIP_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CRASH_SCREEN;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FLOATING_FLORA;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ANYWHERE_CROPS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_PLACEMENT_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FIRE_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BED_EXPLOSION;
    public static final ForgeConfigSpec.BooleanValue DISABLE_FALL_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_DEBUG_SCRAMBLE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_INVENTORY_SCRAMBLE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_CREEPER_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HUD_CORRUPTION;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BINARY_XP;
    public static final ForgeConfigSpec.BooleanValue ENABLE_HEADLESS_PLAYER;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RANDOM_STACK_LIMIT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_MENU_LOCK;
    public static final ForgeConfigSpec.BooleanValue DISABLE_MULTIPLAYER_BUTTON;
    public static final ForgeConfigSpec.BooleanValue ENABLE_FAKE_WORLD_CORRUPTION;
    public static final ForgeConfigSpec.BooleanValue HEX_WORLD_NAME;
    public static final ForgeConfigSpec.BooleanValue FORCE_SURVIVAL;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ANIMATED_MENU;
    public static final ForgeConfigSpec.BooleanValue CORRUPT_MENU_BUTTONS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BUTTON_SHAKE;
    public static final ForgeConfigSpec.BooleanValue ENABLE_GLITCHED_LOGO;
    public static final ForgeConfigSpec.BooleanValue HIDE_SPLASH;
    public static final ForgeConfigSpec.BooleanValue REPLACE_MUSIC;
    public static final ForgeConfigSpec.DoubleValue GLITCH_VOLUME;
    public static final ForgeConfigSpec.BooleanValue DISABLE_VOID_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue DISABLE_CONTROLS_DURING_HUNT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_VANISH_OVERLAY;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RARE_DROPS;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ANAGLYPH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_XRAY_FLASH;
    public static final ForgeConfigSpec.BooleanValue PERMANENT_QUIT_LOCK;
    public static final ForgeConfigSpec.BooleanValue SHOW_WARNING_SCREEN;
    public static final ForgeConfigSpec.BooleanValue ALLOW_ZERO_STACK_LIMIT;
    public static final ForgeConfigSpec.BooleanValue ENABLE_INVENTORY_LOAD_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_RANDOM_CRAFTING;
    public static final ForgeConfigSpec.BooleanValue ENABLE_BROKEN_RECIPES;
    public static final ForgeConfigSpec.BooleanValue ENABLE_VERSION_GLITCH;
    public static final ForgeConfigSpec.BooleanValue ENABLE_ADVANCEMENT_RESET;
    public static final ForgeConfigSpec.IntValue SPAWN_DELAY;
    public static final ForgeConfigSpec.IntValue SPAWN_INTERVAL;
    public static final ForgeConfigSpec.IntValue GLITCH_INTERVAL;
    public static final ForgeConfigSpec.IntValue HUNT_DURATION;
    public static final ForgeConfigSpec.DoubleValue ENTITY_DAMAGE;
    public static final ForgeConfigSpec.DoubleValue ENTITY_HEALTH;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Error 422 genel ayarlari").push("general");
        ENABLE_ENTITY = builder
                .comment("Error 422 varligi kendiliginden spawn olsun mu?")
                .define("enableEntity", true);
        ENABLE_GLITCHES = builder
                .comment("Rastgele glitch olaylari acik olsun mu?")
                .define("enableGlitches", true);
        ENABLE_BLOCK_GLITCHES = builder
                .comment("Bloklarin rastgele degismesi/bozulmasi acik olsun mu?")
                .define("enableBlockGlitches", true);
        ENABLE_LAVA_GLITCH = builder
                .comment("Nadir su -> lav glitch'i acik olsun mu?")
                .define("enableLavaGlitch", true);
        ENABLE_LIGHTNING = builder
                .comment("Gercek yildirim dusmesi glitch'i acik olsun mu? (yikici olabilir)")
                .define("enableLightning", false);
        ENABLE_HOTBAR_GLITCH = builder
                .comment("Hotbar slotlarinin rastgele yer degistirmesi acik olsun mu?")
                .define("enableHotbarGlitch", true);
        ENABLE_TOOLTIP_GLITCH = builder
                .comment("Varlik yakindayken esya tooltip'lerinin bozulmasi acik olsun mu?")
                .define("enableTooltipGlitch", true);
        ENABLE_CRASH_SCREEN = builder
                .comment("Sahte ERROR 422 cokme ekrani gosterilsin mi?")
                .define("enableCrashScreen", true);
        ENABLE_FLOATING_FLORA = builder
                .comment("Ucan/yeralti kaktus, nilufer ve olu cali glitch'i.")
                .define("enableFloatingFlora", true);
        ENABLE_ANYWHERE_CROPS = builder
                .comment("Ekinlerin her blok uzerine dikilebilmesi.")
                .define("enableAnywhereCrops", true);
        ENABLE_PLACEMENT_GLITCH = builder
                .comment("Yerlestirilen bloklarin rastgele baska bir blokla degismesi.")
                .define("enablePlacementGlitch", true);
        ENABLE_FIRE_GLITCH = builder
                .comment("Oyuncunun etrafinda rastgele yangin cikmasi.")
                .define("enableFireGlitch", true);
        ENABLE_BED_EXPLOSION = builder
                .comment("Overworld'de yataklarin nadiren patlamasi.")
                .define("enableBedExplosion", true);
        DISABLE_FALL_DAMAGE = builder
                .comment("Dusme hasarini kapat (creepypasta'daki gibi).")
                .define("disableFallDamage", true);
        ENABLE_DEBUG_SCRAMBLE = builder
                .comment("F3 debug ekraninin bozulmasi.")
                .define("enableDebugScramble", true);
        ENABLE_INVENTORY_SCRAMBLE = builder
                .comment("Glitch sirasinda envanterin karismasi.")
                .define("enableInventoryScramble", true);
        ENABLE_CREEPER_GLITCH = builder
                .comment("Nadir anlik creeper patlamasi.")
                .define("enableCreeperGlitch", true);
        ENABLE_HUD_CORRUPTION = builder
                .comment("Can/aciklik barlarini bozuk goster ve oksijen barini gizle.")
                .define("enableHudCorruption", true);
        ENABLE_BINARY_XP = builder
                .comment("Deneyim seviyesini binary olarak goster.")
                .define("enableBinaryXp", true);
        ENABLE_HEADLESS_PLAYER = builder
                .comment("Oyuncuyu kafasiz goster (kasklar da gorunmez).")
                .define("enableHeadlessPlayer", true);
        ENABLE_RANDOM_STACK_LIMIT = builder
                .comment("Her oturumda 1-99 arasi rastgele bir maksimum stack limiti uygula.")
                .define("enableRandomStackLimit", true);
        ENABLE_MENU_LOCK = builder
                .comment("Varlik avlanirken ESC menusunu gecici olarak kilitle.")
                .define("enableMenuLock", true);
        DISABLE_MULTIPLAYER_BUTTON = builder
                .comment("Ana menudeki Multiplayer butonunu devre disi birak (tam fidelity icin true yap).")
                .define("disableMultiplayerButton", false);
        ENABLE_FAKE_WORLD_CORRUPTION = builder
                .comment("Sahte 'dunya verisi bozuldu' mesaji ve efektleri.")
                .define("enableFakeWorldCorruption", true);
        HEX_WORLD_NAME = builder
                .comment("Dunya olustururken adini hex'e cevir (creepypasta detayi).")
                .define("hexWorldName", true);
        FORCE_SURVIVAL = builder
                .comment("Creative/spectator ve cheats'i engelle, oyuncuyu survival'a zorla.")
                .define("forceSurvival", true);
        ENABLE_ANIMATED_MENU = builder
                .comment("Ana menu arka planini animasyonlu glitch yap.")
                .define("enableAnimatedMenu", true);
        CORRUPT_MENU_BUTTONS = builder
                .comment("Ana menu buton yazilarini boz.")
                .define("corruptMenuButtons", true);
        ENABLE_BUTTON_SHAKE = builder
                .comment("Fare ustune gelince butonlar titresin.")
                .define("enableButtonShake", true);
        ENABLE_GLITCHED_LOGO = builder
                .comment("Minecraft logosunu kirmizi/mavi glitch'li goster ve Java Edition yazisini kaldir.")
                .define("enableGlitchedLogo", true);
        HIDE_SPLASH = builder
                .comment("Ana menudeki sari splash yazisini gizle.")
                .define("hideSplash", true);
        REPLACE_MUSIC = builder
                .comment("Vanilla muzigi kapat ve yerine surekli sentezlenmis glitch ambience cal.")
                .define("replaceMusic", true);
        GLITCH_VOLUME = builder
                .comment("Glitch ambience ses seviyesi (0.0 - 1.0).")
                .defineInRange("glitchVolume", 0.35D, 0.0D, 1.0D);
        DISABLE_VOID_DAMAGE = builder
                .comment("Void hasarini kapat.")
                .define("disableVoidDamage", true);
        DISABLE_CONTROLS_DURING_HUNT = builder
                .comment("Avlanma sirasinda saldiri/kullanma/sol tik ve hotbar scroll'u kilitle.")
                .define("disableControlsDuringHunt", true);
        ENABLE_VANISH_OVERLAY = builder
                .comment("Varlik kaybolunca kisa kirmizi overlay goster.")
                .define("enableVanishOverlay", true);
        ENABLE_RARE_DROPS = builder
                .comment("Varlik kovulunca nadir/unobtainable esyalar dusur.")
                .define("enableRareDrops", true);
        ENABLE_ANAGLYPH = builder
                .comment("Varlik yakinken gercek kirmizi/cyan anaglyph post-process filtresi.")
                .define("enableAnaglyph", true);
        ENABLE_XRAY_FLASH = builder
                .comment("Avlanma sirasinda ara ara X-ray terrain flash efekti.")
                .define("enableXrayFlash", true);
        PERMANENT_QUIT_LOCK = builder
                .comment("Quit ve Save & Quit butonlarini kalici olarak devre disi birak.")
                .define("permanentQuitLock", true);
        SHOW_WARNING_SCREEN = builder
                .comment("Oyun acilisinda ana menuden once fotosensitivite/uyari ekrani goster.")
                .define("showWarningScreen", true);
        ALLOW_ZERO_STACK_LIMIT = builder
                .comment("Rastgele stack limiti 0 olabilsin (0-99 araligi, %5 sifir).")
                .define("allowZeroStackLimit", true);
        ENABLE_INVENTORY_LOAD_GLITCH = builder
                .comment("Envanter acilinca slotlarin bazen bos gorunmesi (yuklenmemis gibi).")
                .define("enableInventoryLoadGlitch", true);
        ENABLE_RANDOM_CRAFTING = builder
                .comment("Crafting bazen rastgele bir esya versin.")
                .define("enableRandomCrafting", true);
        ENABLE_BROKEN_RECIPES = builder
                .comment("Fence ve golden apple tarifleri bozuk olsun (sonuc kaybolur).")
                .define("enableBrokenRecipes", true);
        ENABLE_VERSION_GLITCH = builder
                .comment("Ara ara sahte surum degisimi ve oyuncu modeli (basli/bassiz) degisimi.")
                .define("enableVersionGlitch", true);
        ENABLE_ADVANCEMENT_RESET = builder
                .comment("Basarimlari gercekten sifirla (giriste %20 ve rastgele glitch).")
                .define("enableAdvancementReset", true);

        SPAWN_DELAY = builder
                .comment("Ilk spawn'a kadar gecen tick sayisi (20 tick = 1 saniye).")
                .defineInRange("spawnDelayTicks", 6000, 200, 1000000);
        SPAWN_INTERVAL = builder
                .comment("Spawn'lar arasi ortalama tick sayisi.")
                .defineInRange("spawnIntervalTicks", 12000, 600, 1000000);
        GLITCH_INTERVAL = builder
                .comment("Glitch olaylari arasi ortalama tick sayisi.")
                .defineInRange("glitchIntervalTicks", 1200, 200, 1000000);
        HUNT_DURATION = builder
                .comment("Avlanma suresi (tick). Sure bitince varlik kaybolur.")
                .defineInRange("huntDurationTicks", 600, 100, 100000);
        ENTITY_DAMAGE = builder
                .comment("Varligin saldiri hasari.")
                .defineInRange("entityDamage", 8.0D, 0.0D, 100.0D);
        ENTITY_HEALTH = builder
                .comment("Varligin maksimum cani.")
                .defineInRange("entityHealth", 100.0D, 1.0D, 1000.0D);
        builder.pop();

        SPEC = builder.build();
    }

    private Error422Config() {
    }
}
