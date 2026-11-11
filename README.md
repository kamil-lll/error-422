# Error 422 — Minecraft 1.21.1 (Forge)

An original Minecraft horror mod inspired by the “corrupted version” creepypasta.

**[🇬🇧 English](#english)** · **[🇹🇷 Türkçe](#türkçe)**

> ⚠️ **Warning:** contains sudden sounds, screen shake, flashing effects and a fake crash screen.
> Every effect can be disabled in `config/error422-common.toml`.

**Links**

- [GitHub repository](https://github.com/kamil-lll/error-422)
- [Latest jar (GitHub Releases)](https://github.com/kamil-lll/error-422/releases/latest)
- [Modrinth](https://modrinth.com/project/error-422-unknown-version) — awaiting review
- [CurseForge release notes](CURSEFORGE.md)

---

## English

### What does it do?

Error 422 is a corrupted version of the game that has come alive and attached itself to your world:

- After some time in a world it appears and starts **watching** you.
- Then it **cuts the signal**: fake crash screen, corrupted chat errors, wrong sounds.
- Then it **hunts**: it is fast, can kill in a few hits, and is immune to direct attacks.
- Survive for 30 seconds and it vanishes, dropping a **Glitch Fragment**.
- An **Error Report** lets you sever the connection and banish it permanently.
- It notices creative players: says “CHEATS DETECTED” and leaves (it does not actually close the game).

### Installation

1. Install **Forge 52.x** for **Minecraft 1.21.1**.
2. Drop `error422-1.2.17.jar` from the [releases page](https://github.com/kamil-lll/error-422/releases/latest) into `.minecraft/mods`.
3. Launch the game with the Forge profile.

### Language support

- The mod follows the **game language**: English if the game is English, Turkish if the game is Turkish.
- Translation files: `assets/error422/lang/en_us.json` and `tr_tr.json`.
- Translated strings: chat messages, action bar warnings, command feedback, the startup warning screen, the fake crash screen, and main menu / pause menu button labels.
- Change the language in-game via **Options → Language**; it applies on the next screen init or chat message.
- If a key is missing from a language file, English is shown.
- Technical stack traces (`NullPointerException` lines and friends) stay in English on purpose.

### Building

Requirements:

- **JDK 21**
- **Gradle 8.8+**

```bash
cd error-422
gradle build
```

Output: `build/libs/error422-1.2.17.jar`

The first build downloads Forge/Minecraft dependencies; the mod itself does not use the internet while playing.

### Startup warning

- A **static warning screen** appears on launch, **before** the main menu.
- It states that the mod contains flashing lights, rapid colour changes, screen shake, red/cyan 3D and glitch effects.
- It says **people with photosensitive epilepsy or light sensitivity should not play**.
- The screen is **non-animated** and high-contrast; the glitch audio is muted while it is open.
- The `Continue` button takes you to the main menu; closing the window is always allowed.
- The warning is shown on the **first main menu** only; returning from Options does not show it again.
- Skip it with: `showWarningScreen=false`.

### Constant glitch audio

- Vanilla music is disabled and replaced by a **continuous glitch ambience**.
- The sound is **synthesised at runtime**: white noise + low rumble + random corrupted signal bursts + occasional high hiss.
- There is no audio file; it is generated entirely in code and loops forever.
- Disable with `replaceMusic=false`; volume via `glitchVolume` (default 0.35).
- It plays on the **MUSIC** channel, so the vanilla **Music** slider controls it. Vanilla music itself is also blocked.
- If the music volume is 0 the glitch audio stops; raising it again restarts the stream automatically (the 0→100 bug is fixed).

### Menu visuals

- **Main menu:** dense red/blue glitch code rain; the texture keeps **scrolling down** with random red/blue bars and flicker on top.
- **Minecraft logo:** drawn with red/blue glitch ghosts and animated slices; **no “Java Edition” text**, just “Minecraft”.
- **Yellow splash text removed** (`hideSplash`).
- **Main menu buttons:** dark red button texture, bright red hover and a dark disabled state; the labels are corrupted (Singleplayer, Multiplayer, Realms, Options, Quit).
- **On mouse hover:** buttons **shake** slightly.
- **Void damage disabled** (`disableVoidDamage`).
- **Random stack limit 0–99:** 5% chance of **0**; with 0, items cannot be picked up or stacked. Turn 0 off with `allowZeroStackLimit=false`.
- **Inventory load glitch:** 20% chance the slots look **empty** for 4–8 seconds after opening the inventory; items are not lost, only not rendered (`enableInventoryLoadGlitch`).
- **Crafting glitch:** 15% chance of a random item; fence and golden apple recipes are broken (result is lost). `enableRandomCrafting`, `enableBrokenRecipes`.
- **Version / player model glitch:** every 1–3 minutes it flips between `Minecraft 1.2.1` (headless) and `Minecraft 1.9.1` (with head); the fake version shows in the action bar and F3. `enableVersionGlitch`. The real game version stays 1.21.1.
- **Advancements are really reset:** 20% chance on join and on random glitch events the whole advancement progress is revoked; `enableAdvancementReset`.
- **Normal music fully disabled:** `MusicManagerMixin` cancels vanilla music; only the glitch noise plays.
- **Floating cacti:** `CactusBlockMixin` keeps them from breaking on neighbour updates, so they stay in the air. Test with `/error422 flora`.
- **While hunting:** attack/use/left-click and hotbar scroll are locked (`disableControlsDuringHunt`).
- **When the entity vanishes:** a short red overlay (`enableVanishOverlay`).
- **When banished:** it drops rare/unobtainable items (`enableRareDrops`).
- **Real anaglyph (red/cyan 3D):** a post-process shader kicks in when the entity is close; offset 0.01, colour boost, vignette and scanlines make it obvious. While active the `ANAGLYPH // ERROR 422` action message appears (`enableAnaglyph`).
- **X-ray flashing:** during a hunt, solid/cutout terrain disappears for 12–25 ticks; plus a green scan overlay, the `X-RAY // SIGNAL LOST` action message and a sound (`enableXrayFlash`).
- **Permanent quit lock:** the main menu Quit button and the pause menu Save & Quit are disabled; you have to close the window to leave (`permanentQuitLock`).
- **Select World and menus:** **bedrock** background instead of dirt.
- All menu textures were produced from scratch for this project; no vanilla or wiki images were copied.
- Another resource pack that overrides these files may take visual priority.
- Disable with: `enableAnimatedMenu`, `corruptMenuButtons`, `enableButtonShake`, `enableGlitchedLogo`.

#### Tips

- Within 48 blocks of the entity the `ANAGLYPH` action message and red/cyan ghosting appear.
- During a hunt you get the `X-RAY` message, a green scan overlay and the terrain flash.
- If something is missing, send `latest.log`; look for `GameRendererMixin`, `LevelRendererMixin` and `anaglyph` lines.

### World creation

- The world name is automatically converted to **hex**: `Apple` → `4170706c65`.
- New worlds are created as **survival with cheats disabled**.
- If **creative/spectator** is detected in-game, the player is put back into survival and sees `CHEATS DETECTED`.
- Worlds that already have cheats enabled keep the flag; creative/spectator is still blocked. For the cleanest experience, create a new world.

### First encounter

- By default the first spawn happens after **5 minutes** (6000 ticks).
- You can summon it immediately with the **Error Core** from the creative inventory.
- Or with the command: `/error422 spawn`
- For testing: `/error422 glitch`

### Survival loop

1. **Watching:** the entity appears in the distance, watches you and occasionally steps closer.
2. **Glitch:** the screen is briefly covered by a fake `ERROR 422` crash screen; controls lock.
3. **Hunting:** the entity chases you. It applies darkness and slowness, and the screen shakes.
4. **Vanishing:** survive 30 seconds and it disappears, leaving 1–3 Glitch Fragments and sometimes Corrupted Data.
5. **Severing the connection:** use an Error Report next to the entity to banish it permanently.

### Items

| Item | Description |
|---|---|
| **Glitch Fragment** | Drops when you survive a hunt or when the entity is banished. |
| **Corrupted Data** | Rare residue; used in the Error Report recipe. |
| **Error Report** | Severs the entity's connection. Recipe: **paper + redstone + glitch fragment**. |
| **Error Core** | Creative/test item; summons the entity when used. |

### Commands

| Command | Effect |
|---|---|
| `/error422 spawn` | Summons Error 422 near you |
| `/error422 glitch` | Triggers a random glitch event |
| `/error422 banish` | Banishes the current entity |
| `/error422 status` | Shows the entity state |

Commands require permission level 2.

### Glitch events

- Corrupted error dumps (`§k` obfuscated text in chat)
- Wrong sounds
- Random short effects (nausea, blindness, slowness, darkness, rarely levitation)
- Time skips
- Random pushes
- Block swaps
- Floating/underground cacti, lily pads and dead bushes
- Planting crops on top of any block; crops do not break on neighbour updates and can sit side by side
- Random fire
- Placing a block sometimes turns it into a random other block
- Beds occasionally exploding in the Overworld
- No fall damage (default)
- Corrupted F3 debug screen
- Inventory scrambling during glitches
- Rare instant creeper
- The entity occasionally teleporting next to/behind you
- Corrupted health/hunger bars and a missing air bar
- Experience level shown in binary
- Headless player model (helmets are invisible too)
- Random maximum stack limit per session (1–99)
- Temporary ESC/menu lock during a hunt
- Fake “world data corrupted” message and effects
- Optional: disabling the Multiplayer button in the main menu
- Rare water → lava conversion
- Rare real lightning (off by default)
- Fake “advancements reset” message
- Hotbar slot scrambling
- Breaking a block can rarely create water/lava
- Item names scrambled while the entity is near

### Config

`config/error422-common.toml`:

- `enableEntity` — should the entity spawn?
- `enableGlitches` — random glitch events
- `enableBlockGlitches` — block corruption
- `enableLavaGlitch` — water → lava
- `enableLightning` — real lightning (off by default)
- `enableHotbarGlitch` — hotbar scrambling
- `enableTooltipGlitch` — item name corruption
- `enableCrashScreen` — fake crash screen
- `spawnDelayTicks`, `spawnIntervalTicks`, `glitchIntervalTicks`, `huntDurationTicks`
- `entityDamage`, `entityHealth`
- `enableFloatingFlora`, `enableAnywhereCrops`, `enablePlacementGlitch`
- `enableFireGlitch`, `enableBedExplosion`, `enableCreeperGlitch`
- `disableFallDamage`, `enableDebugScramble`, `enableInventoryScramble`
- `enableHudCorruption`, `enableBinaryXp`, `enableHeadlessPlayer`
- `enableRandomStackLimit`, `enableMenuLock`, `disableMultiplayerButton`
- `enableFakeWorldCorruption`
- `hexWorldName`, `forceSurvival`

### Safe adaptations

Some items from the creepypasta are intentionally interpreted in a **safe** way:

- **Really crashing the game:** replaced by a short fake `ERROR 422` crash screen; the game does not close.
- **Really corrupting world files:** files are never touched; only a fake “world data corrupted” message/effect.
- **Permanent “Quit Game” lock:** implemented (`permanentQuitLock`); main menu Quit and pause menu Save & Quit are disabled. Turn it off in config if it is too much.
- **Random crafting results:** implemented (`enableRandomCrafting`); the crafted result is removed and replaced by a random item.
- **Stack limit 0:** implemented (`allowZeroStackLimit`); only a 5% chance and only for singleplayer discomfort. With 0, items cannot be picked up.
- **Disabling multiplayer:** implemented (`disableMultiplayerButton`); you can turn it off in config.
- **Advancements reset:** real advancement data is revoked (`enableAdvancementReset`).

### Copyright

This mod is an **original gameplay interpretation inspired by** the creepypasta; most of the world glitches from the “Odd Occurrences” section are adapted.
No story text, images or sounds were copied; all code and textures were produced from scratch for this project.
Minecraft is a trademark of Mojang/Microsoft. This mod is unofficial.

---

## Türkçe

“Bozuk sürüm” creepypasta’sından esinlenilmiş, özgün bir Minecraft korku modu.

> ⚠️ **Uyarı:** Ani sesler, ekran sarsıntısı, yanıp sönen efektler ve sahte bir çökme ekranı içerir.
> Rahatsız edici bulursan `config/error422-common.toml` üzerinden efektleri kapatabilirsin.

**Bağlantılar**

- [GitHub deposu](https://github.com/kamil-lll/error-422)
- [Son sürüm jar (GitHub Releases)](https://github.com/kamil-lll/error-422/releases/latest)
- [Modrinth](https://modrinth.com/project/error-422-unknown-version) — yayın incelemesi bekliyor
- [CurseForge yayın notları](CURSEFORGE.md)

### Ne yapıyor?

Error 422, dünyaya musallat olan bozuk bir sürümün canlanmış hâlidir:

- Dünyada bir süre geçtikten sonra ortaya çıkar; önce **izler**.
- Ardından **sinyali keser**: sahte çökme ekranı, bozuk sohbet hataları, yanlış sesler.
- Sonra **avlanmaya** başlar: hızlıdır, birkaç vuruşta öldürebilir, doğrudan saldırıyla zarar görmez.
- 30 saniye hayatta kalırsan kaybolur ve **Glitch Parçası** düşürür.
- **Hata Raporu** ile bağlantısını kesip kalıcı olarak uzaklaştırabilirsin.
- Creative oyuncuları fark eder: “CHEATS DETECTED” deyip gider (gerçekten oyunu kapatmaz).

### Kurulum

1. **Minecraft 1.21.1** için **Forge 52.x** kur.
2. [sürüm sayfasından](https://github.com/kamil-lll/error-422/releases/latest) indirdiğin `error422-1.2.17.jar` dosyasını `.minecraft/mods` klasörüne at.
3. Forge profiliyle oyunu başlat.

### Dil desteği

- Mod, **oyunun dilini** takip eder: oyun İngilizce ise **İngilizce**, Türkçe ise **Türkçe** mesaj gösterir.
- Çeviri dosyaları: `assets/error422/lang/en_us.json` ve `tr_tr.json`.
- Çevrilen metinler: sohbet mesajları, aksiyon çubuğu uyarıları, komut geri bildirimleri, açılış uyarı ekranı, sahte çökme ekranı ve ana menü/pause buton yazıları.
- Dil, oyun içinde **Options → Language** üzerinden değiştirilir; değişiklik bir sonraki ekran açılışında/sohbet mesajında görünür.
- Dil dosyasında karşılığı olmayan bir anahtar olursa İngilizce metin gösterilir.
- Teknik hata dökümleri (stack trace taklidi, `NullPointerException` satırları) kasıtlı olarak İngilizce kalır.

### Derleme

Gereksinimler:

- **JDK 21**
- **Gradle 8.8+**

```bash
cd error-422
gradle build
```

Çıktı: `build/libs/error422-1.2.17.jar`

İlk derleme Forge/Minecraft bağımlılıklarını indirir; modun kendisi oyun sırasında internet kullanmaz.

### Açılış uyarısı

- Oyun açıldığında ana menüden **önce sabit bir uyarı ekranı** çıkar.
- Uyarı; yanıp sönen ışıklar, hızlı renk değişimleri, ekran sarsıntısı, kırmızı/cyan 3D ve glitch efektleri içerdiğini belirtir.
- **Fotoğrafik epilepsi veya ışığa hassasiyeti olanların oynamaması** gerektiği yazar.
- Uyarı ekranı **animasyonsuz** ve yüksek kontrastlıdır; bu sırada glitch sesi de susar.
- `Devam Et / Continue` butonu ana menüye geçirir; istemeyen pencereyi kapatabilir.
- Uyarı, oyun açılışında **ilk ana menüde** gösterilir; Ayarlar’dan dönüşte tekrar çıkmaz.
- Atlamak için: `showWarningScreen=false`.

### Sürekli glitch sesi

- Vanilla müzik kapatılır; yerine **sürekli, kesintisiz glitch ambience** çalar.
- Ses **runtime’da sentezlenir**: beyaz gürültü + düşük uğultu + rastgele bozuk sinyal patlamaları + ara ara yüksek cızırtı.
- Ses dosyası yoktur; tamamen kodla üretilir, bu yüzden sonsuz döngüde çalar.
- Kapatmak için `replaceMusic=false`; ses seviyesi `glitchVolume` (varsayılan 0.35).
- Ses **MUSIC** kanalındadır; oyunun **Müzik** ses kaydırıcısı kontrol eder. Vanilla müzik ayrıca engellenir.
- Müzik ses seviyesi 0 ise glitch sesi durur; tekrar yükselttiğinde stream otomatik yeniden başlar (0→100 sorunu düzeltildi).

### Menü görselleri

- **Ana menü:** yoğun kırmızı/mavi glitch kod yağmuru; doku sürekli **aşağı kayar** ve rastgele kırmızı/mavi barlar/flicker eklenir.
- **Minecraft logosu:** kırmızı/mavi glitch hayaletleri ve animasyonlu dilimlerle çizilir; **“Java Edition” yazısı yok**, sadece “Minecraft”.
- **Sarı splash yazısı kaldırıldı** (`hideSplash`).
- **Ana menü butonları:** koyu kırmızı buton dokusu, parlak kırmızı hover ve koyu disabled hâli; yazılar bozulmuştur (Singleplayer, Multiplayer, Realms, Options, Quit).
- **Fare üstüne gelince:** butonlar hafifçe **titrer**.
- **Void hasarı kapalı** (`disableVoidDamage`).
- **Rastgele stack limiti 0–99:** %5 ihtimalle **0** olur; 0 ise eşyalar toplanmaz/stack olmaz. `allowZeroStackLimit=false` ile 0 kapatılabilir.
- **Envanter yüklenmeme glitch'i:** envanter açılınca %20 ihtimalle slotlar 4–8 saniye **boş görünür**; eşyalar kaybolmaz, sadece render edilmez (`enableInventoryLoadGlitch`).
- **Crafting glitch'i:** %15 ihtimalle rastgele bir eşya verir; fence ve golden apple tarifleri bozuktur (sonuç kaybolur). `enableRandomCrafting`, `enableBrokenRecipes`.
- **Sürüm / oyuncu modeli glitch'i:** 1–3 dakikada bir `Minecraft 1.2.1` (başsız) ile `Minecraft 1.9.1` (başlı) arasında geçer; aksiyon çubuğunda ve F3’te sahte sürüm görünür. `enableVersionGlitch`. Gerçek oyun sürümü 1.21.1 olarak kalır.
- **Başarımlar gerçekten sıfırlanır:** oyuna girişte %20 ihtimalle ve rastgele glitch olayında tüm advancement ilerlemesi revoke edilir; `enableAdvancementReset`.
- **Normal müzik tamamen kapalı:** `MusicManagerMixin` vanilla müzik başlatmayı iptal eder; sadece glitch noise çalar.
- **Uçan kaktüsler:** `CactusBlockMixin` sayesinde komşu güncellemesinde kırılmaz, havada kalır. `/error422 flora` ile test edebilirsin.
- **Avlanma sırasında** saldırı/kullanma/sol tık ve hotbar scroll kilidi (`disableControlsDuringHunt`).
- **Varlık kaybolunca** kısa kırmızı overlay (`enableVanishOverlay`).
- **Kovulunca** nadir/unobtainable eşyalar düşer (`enableRareDrops`).
- **Gerçek anaglyph (kırmızı/cyan 3D):** varlık yakınındayken post-process shader devreye girer; offset 0.01, renk boost, vignette ve scanline ile belirginleştirildi. Aktifken `ANAGLYPH // ERROR 422` aksiyon mesajı görünür (`enableAnaglyph`).
- **X-ray flashing:** avlanma sırasında katı/cutout terrain 12–25 tick görünmez olur; ayrıca yeşil tarama overlay’i, `X-RAY // SIGNAL LOST` aksiyon mesajı ve ses çalar (`enableXrayFlash`).
- **Kalıcı Quit kilidi:** ana menüdeki Quit ve pause menüsündeki Save & Quit butonları devre dışı; oyundan çıkmak için pencereyi kapatman gerekir (`permanentQuitLock`).
- **Select World ve menüler:** dirt yerine **bedrock** arka plan.
- Tüm menü dokuları bu proje için sıfırdan üretilmiştir; vanilla veya wiki görselleri kopyalanmamıştır.
- Başka bir resource pack bu dosyaları override ediyorsa onun görseli öncelikli olabilir.
- Kapatmak için: `enableAnimatedMenu`, `corruptMenuButtons`, `enableButtonShake`, `enableGlitchedLogo`.

#### İpuçları

- Varlığın 48 blok yakınına gelince `ANAGLYPH` aksiyon mesajı ve kırmızı/cyan kaymalar görünür.
- Avlanmada `X-RAY` mesajı, yeşil tarama overlay’i ve terrain flash’i görünür.
- Görünmezse `latest.log` gönder; `GameRendererMixin`, `LevelRendererMixin` ve `anaglyph` satırlarına bakılır.

### Dünya oluşturma

- Dünya adı otomatik olarak **hex**'e çevrilir: `Apple` → `4170706c65`.
- Yeni dünyalar **survival + cheats kapalı** oluşturulur.
- Oyun içinde **creative/spectator** algılanırsa oyuncu survival'a geri alınır ve `CHEATS DETECTED` mesajı görür.
- Mevcut cheats açık dünyalarda cheats bayrağı kalır; ancak creative/spectator engellenir. Tam temiz deneyim için yeni dünya oluştur.

### İlk karşılaşma

- Varsayılan olarak ilk spawn **5 dakika** (6000 tick) sonra gerçekleşir.
- Creative envanterindeki **Hata Çekirdeği** ile hemen çağırabilirsin.
- Veya komutla: `/error422 spawn`
- Test için: `/error422 glitch`

### Hayatta kalma döngüsü

1. **İzleme:** Varlık uzakta belirir, seni izler, ara ara yaklaşır.
2. **Glitch:** Ekran kısa süreliğine sahte bir `ERROR 422` çökme ekranıyla kaplanır; kontrol kilitlenir.
3. **Avlanma:** Varlık peşine düşer. Karanlık ve yavaşlık efektleri uygular, ekran sarsılır.
4. **Kaybolma:** 30 saniye dayanırsan kaybolur; 1–3 Glitch Parçası ve bazen Bozuk Veri bırakır.
5. **Bağlantıyı kesme:** Hata Raporu’nu varlığın yanında kullanarak onu kalıcı olarak kovabilirsin.

### Eşyalar

| Eşya | Açıklama |
|---|---|
| **Glitch Parçası** | Avlanmadan sağ çıkınca veya varlık kovulunca düşer. |
| **Bozuk Veri** | Nadir kalıntı; Hata Raporu tarifinde kullanılır. |
| **Hata Raporu** | Varlığın bağlantısını keser. Tarif: **kağıt + redstone + glitch parçası**. |
| **Hata Çekirdeği** | Creative/test eşyası; kullanınca varlığı çağırır. |

### Komutlar

| Komut | Etki |
|---|---|
| `/error422 spawn` | Yakınında Error 422 çağırır |
| `/error422 glitch` | Rastgele bir glitch olayı tetikler |
| `/error422 banish` | Mevcut varlığı kovar |
| `/error422 status` | Varlığın durumunu gösterir |

Komutlar için yetki seviyesi 2 gerekir.

### Glitch olayları

- Bozuk hata dökümleri (sohbette `§k` ile şifreli metinler)
- Yanlış sesler
- Rastgele kısa efektler (bulantı, körlük, yavaşlık, karanlık, nadiren yükselme)
- Zaman atlamaları
- Rastgele itmeler
- Blok değişimleri
- Uçan/yeraltı kaktüsleri, nilüferler ve ölü çalılar
- Ekinleri her bloğun üzerine dikebilme; ekinler komşu güncellemesinde kırılmaz ve yan yana kalabilir
- Rastgele yangın
- Blok yerleştirince rastgele başka bir blokla değişme
- Yatakların Overworld'de nadiren patlaması
- Düşme hasarının olmaması (varsayılan)
- F3 debug ekranının bozulması
- Envanterin glitch sırasında karışması
- Nadir anlık creeper
- Varlığın ara ara yanına/arkana ışınlanması
- Bozuk can/açlık barları ve eksik oksijen barı
- Deneyim seviyesinin binary gösterimi
- Başsız oyuncu modeli (kasklar da görünmez)
- Oturum başına rastgele maksimum stack limiti (1–99)
- Avlanma sırasında geçici ESC/menü kilidi
- Sahte “dünya verisi bozuldu” mesajı ve efektleri
- Opsiyonel: ana menüde Multiplayer butonunu kapatma
- Nadir su → lav dönüşümü
- Nadiren gerçek yıldırım (varsayılan kapalı)
- Sahte “başarımlar sıfırlandı” mesajı
- Hotbar slot karışması
- Blok kırınca küçük ihtimalle su/lav oluşması
- Varlık yakındayken eşya isimlerinin bozulması

### Config

`config/error422-common.toml`:

- `enableEntity` — varlık spawn olsun mu?
- `enableGlitches` — rastgele glitch olayları
- `enableBlockGlitches` — blok bozulmaları
- `enableLavaGlitch` — su → lav
- `enableLightning` — gerçek yıldırım (varsayılan kapalı)
- `enableHotbarGlitch` — hotbar karışması
- `enableTooltipGlitch` — eşya isimlerinin bozulması
- `enableCrashScreen` — sahte çökme ekranı
- `spawnDelayTicks`, `spawnIntervalTicks`, `glitchIntervalTicks`, `huntDurationTicks`
- `entityDamage`, `entityHealth`
- `enableFloatingFlora`, `enableAnywhereCrops`, `enablePlacementGlitch`
- `enableFireGlitch`, `enableBedExplosion`, `enableCreeperGlitch`
- `disableFallDamage`, `enableDebugScramble`, `enableInventoryScramble`
- `enableHudCorruption`, `enableBinaryXp`, `enableHeadlessPlayer`
- `enableRandomStackLimit`, `enableMenuLock`, `disableMultiplayerButton`
- `enableFakeWorldCorruption`
- `hexWorldName`, `forceSurvival`

### Güvenli uyarlamalar

Bu bölüm, creepypasta’daki bazı maddelerin bilinçli olarak **güvenli** biçimde yorumlandığını açıklar:

- **Gerçek oyun çökmesi:** Yerine kısa süreli sahte `ERROR 422` çökme ekranı var; oyun kapanmıyor.
- **Dünya dosyasının gerçekten bozulması:** Dosyalara dokunulmuyor; yerine sahte “dünya verisi bozuldu” mesajı/efektleri var.
- **Kalıcı “Quit Game” kilidi:** Uygulandı (`permanentQuitLock`); ana menüdeki Quit ve pause menüsündeki Save & Quit devre dışı kalır. Rahatsız ediciyse config’ten kapat.
- **Crafting’in rastgele sonuç vermesi:** Uygulandı (`enableRandomCrafting`); craft sonrası sonuç silinip rastgele eşya verilir.
- **Stack limiti 0:** Uygulandı (`allowZeroStackLimit`); yalnızca %5 ihtimalle ve yalnızca tek oyunculu rahatsızlık amacıyla. 0 gelirse eşyalar toplanmaz.
- **Multiplayer’ın kapatılması:** Uygulandı (`disableMultiplayerButton`); config’ten kapatabilirsin.
- **Başarımların sıfırlanması:** Gerçek advancement verisi revoke edilir (`enableAdvancementReset`).

### Telif

Bu mod, ilgili creepypasta’dan **esinlenilmiş özgün bir oynanış yorumudur**; "Odd Occurrences" bölümündeki dünya glitch’lerinin büyük kısmı uyarlanmıştır.
Hikâye metni, görseller veya sesler kopyalanmamıştır; tüm kod ve dokular bu proje için sıfırdan üretilmiştir.
Minecraft, Mojang/Microsoft’un tescilli markasıdır. Bu mod resmî değildir.
