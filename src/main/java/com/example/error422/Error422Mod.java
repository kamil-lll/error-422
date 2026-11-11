package com.example.error422;

import com.example.error422.config.Error422Config;
import com.example.error422.registry.ModEntities;
import com.example.error422.registry.ModItems;
import com.example.error422.registry.ModTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Error 422 - bozuk bir Minecraft surumunun canlanmis glitch varligi.
 * Ilgili creepypasta'dan esinlenilmis ozgun bir oynanis yorumudur.
 */
@Mod(Error422Mod.MODID)
public class Error422Mod {

    public static final String MODID = "error422";

    public Error422Mod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModEntities.ENTITY_TYPES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModTabs.TABS.register(modBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Error422Config.SPEC);
    }
}
