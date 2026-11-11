# Error 422 — Minecraft 1.21.1 (Forge)

“Bozuk sürüm” creepypasta’sından esinlenilmiş, özgün bir Minecraft korku modu.

> ⚠️ **Uyarı:** Ani sesler, ekran sarsıntısı, yanıp sönen efektler ve sahte bir çökme ekranı içerir.
> Rahatsız edici bulursan `config/error422-common.toml` üzerinden efektleri kapatabilirsin.

## Ne yapıyor?

Error 422, dünyaya musallat olan bozuk bir sürümün canlanmış hâlidir:

- Dünyada bir süre geçtikten sonra ortaya çıkar; önce **izler**.
- Ardından **sinyali keser**: sahte çökme ekranı, bozuk sohbet hataları, yanlış sesler.
- Sonra **avlanmaya** başlar: hızlıdır, birkaç vuruşta öldürebilir, doğrudan saldırıyla zarar görmez.
- 30 saniye hayatta kalırsan kaybolur ve **Glitch Parçası** düşürür.
- **Hata Raporu** ile bağlantısını kesip kalıcı olarak uzaklaştırabilirsin.
- Creative oyuncuları fark eder: “CHEATS DETECTED” deyip gider (gerçekten oyunu kapatmaz).

## Kurulum

1. **Minecraft 1.21.1** için **Forge 52.x** kur.
2. `error422-1.2.17.jar` dosyasını `.minecraft/mods` klasörüne at.
3. Forge profiliyle oyunu başlat.

## Dil desteği

- Mod, **oyunun dilini** takip eder: oyun İngilizce ise **İngilizce**, Türkçe ise **Türkçe** mesaj gösterir.
- Çeviri dosyaları: `assets/error422/lang/en_us.json` ve `tr_tr.json`.
- Çevrilen metinler: sohbet mesajları, aksiyon çubuğu uyarıları, komut geri bildirimleri, açılış uyarı ekranı, sahte çökme ekranı ve ana menü/pause buton yazıları.
- Dil, oyun içinde **Options → Language** üzerinden değiştirilir; değişiklik bir sonraki ekran açılışında/sohbet mesajında görünür.
- Dil dosyasında karşılığı olmayan bir anahtar olursa İngilizce metin gösterilir.
- Teknik hata dökümleri (stack trace taklidi, `NullPointerException` satırları) kasıtlı olarak İngilizce kalır.

## Derleme

Gereksinimler:

- **JDK 21**
- **Gradle 8.8+**

```bash
cd error-422
gradle build
```

Çıktı: `build/libs/error422-1.2.17.jar`

İlk derleme Forge/Minecraft bağımlılıklarını indirir; modun kendisi oyun sırasında internet kullanmaz.

## Açılış uyarısı

- Oyun açıldığında ana menüden **önce sabit bir uyarı ekranı** çıkar.
- Uyarı; yanıp sönen ışıklar, hızlı renk değişimleri, ekran sarsıntısı, kırmızı/cyan 3D ve glitch efektleri içerdiğini belirtir.
- **Fotoğrafik epilepsi veya ışığa hassasiyeti olanların oynamaması** gerektiği yazar.
- Uyarı ekranı **animasyonsuz** ve yüksek kontrastlıdır; bu sırada glitch sesi de susar.
- `Devam Et / Continue` butonu ana menüye geçirir; istemeyen pencereyi kapatabilir.
- Uyarı, oyun açılışında **ilk ana menüde** gösterilir; Ayarlar’dan dönüşte tekrar çıkmaz.
- Atlamak için: `showWarningScreen=false`.

## Sürekli glitch sesi

- Vanilla müzik kapatılır; yerine **sürekli, kesintisiz glitch ambience** çalar.
- Ses **runtime’da sentezlenir**: beyaz gürültü + düşük uğultu + rastgele bozuk sinyal patlamaları + ara ara yüksek cızırtı.
- Ses dosyası yoktur; tamamen kodla üretilir, bu yüzden sonsuz döngüde çalar.
- Kapatmak için `replaceMusic=false`; ses seviyesi `glitchVolume` (varsayılan 0.35).
- Ses **MUSIC** kanalındadır; oyunun **Müzik** ses kaydırıcısı kontrol eder. Vanilla müzik ayrıca engellenir.
- Müzik ses seviyesi 0 ise glitch sesi durur; tekrar yükselttiğinde stream otomatik yeniden başlar (0→100 sorunu düzeltildi).

## Menü görselleri

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

### İpuçları

- Varlığın 48 blok yakınına gelince `ANAGLYPH` aksiyon mesajı ve kırmızı/cyan kaymalar görünür.
- Avlanmada `X-RAY` mesajı, yeşil tarama overlay’i ve terrain flash’i görünür.
- Görünmezse `latest.log` gönder; `GameRendererMixin`, `LevelRendererMixin` ve `anaglyph` satırlarına bakılır.

## Dünya oluşturma

- Dünya adı otomatik olarak **hex**'e çevrilir: `Apple` → `4170706c65`.
- Yeni dünyalar **survival + cheats kapalı** oluşturulur.
- Oyun içinde **creative/spectator** algılanırsa oyuncu survival'a geri alınır ve `CHEATS DETECTED` mesajı görür.
- Mevcut cheats açık dünyalarda cheats bayrağı kalır; ancak creative/spectator engellenir. Tam temiz deneyim için yeni dünya oluştur.

## İlk karşılaşma

- Varsayılan olarak ilk spawn **5 dakika** (6000 tick) sonra gerçekleşir.
- Creative envanterindeki **Hata Çekirdeği** ile hemen çağırabilirsin.
- Veya komutla: `/error422 spawn`
- Test için: `/error422 glitch`

## Hayatta kalma döngüsü

1. **İzleme:** Varlık uzakta belirir, seni izler, ara ara yaklaşır.
2. **Glitch:** Ekran kısa süreliğine sahte bir `ERROR 422` çökme ekranıyla kaplanır; kontrol kilitlenir.
3. **Avlanma:** Varlık peşine düşer. Karanlık ve yavaşlık efektleri uygular, ekran sarsılır.
4. **Kaybolma:** 30 saniye dayanırsan kaybolur; 1–3 Glitch Parçası ve bazen Bozuk Veri bırakır.
5. **Bağlantıyı kesme:** Hata Raporu’nu varlığın yanında kullanarak onu kalıcı olarak kovabilirsin.

## Eşyalar

| Eşya | Açıklama |
|---|---|
| **Glitch Parçası** | Avlanmadan sağ çıkınca veya varlık kovulunca düşer. |
| **Bozuk Veri** | Nadir kalıntı; Hata Raporu tarifinde kullanılır. |
| **Hata Raporu** | Varlığın bağlantısını keser. Tarif: **kağıt + redstone + glitch parçası**. |
| **Hata Çekirdeği** | Creative/test eşyası; kullanınca varlığı çağırır. |

## Komutlar

| Komut | Etki |
|---|---|
| `/error422 spawn` | Yakınında Error 422 çağırır |
| `/error422 glitch` | Rastgele bir glitch olayı tetikler |
| `/error422 banish` | Mevcut varlığı kovar |
| `/error422 status` | Varlığın durumunu gösterir |

Komutlar için yetki seviyesi 2 gerekir.

## Glitch olayları

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

## Config

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

## Güvenli uyarlamalar

Bu bölüm, creepypasta’daki bazı maddelerin bilinçli olarak **güvenli** biçimde yorumlandığını açıklar:

- **Gerçek oyun çökmesi:** Yerine kısa süreli sahte `ERROR 422` çökme ekranı var; oyun kapanmıyor.
- **Dünya dosyasının gerçekten bozulması:** Dosyalara dokunulmuyor; yerine sahte “dünya verisi bozuldu” mesajı/efektleri var.
- **Kalıcı “Quit Game” kilidi:** Uygulandı (`permanentQuitLock`); ana menüdeki Quit ve pause menüsündeki Save & Quit devre dışı kalır. Rahatsız ediciyse config’ten kapat.
- **Crafting’in rastgele sonuç vermesi:** Uygulandı (`enableRandomCrafting`); craft sonrası sonuç silinip rastgele eşya verilir.
- **Stack limiti 0:** Uygulandı (`allowZeroStackLimit`); yalnızca %5 ihtimalle ve yalnızca tek oyunculu rahatsızlık amacıyla. 0 gelirse eşyalar toplanmaz.
- **Multiplayer’ın kapatılması:** Uygulandı (`disableMultiplayerButton`); config’ten kapatabilirsin.
- **Başarımların sıfırlanması:** Gerçek advancement verisi revoke edilir (`enableAdvancementReset`).

## Telif

Bu mod, ilgili creepypasta’dan **esinlenilmiş özgün bir oynanış yorumudur**; "Odd Occurrences" bölümündeki dünya glitch’lerinin büyük kısmı uyarlanmıştır.
Hikâye metni, görseller veya sesler kopyalanmamıştır; tüm kod ve dokular bu proje için sıfırdan üretilmiştir.
Minecraft, Mojang/Microsoft’un tescilli markasıdır. Bu mod resmî değildir.
