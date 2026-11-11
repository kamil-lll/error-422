package com.example.error422.registry;

import com.example.error422.Error422Mod;
import com.example.error422.item.ErrorCoreItem;
import com.example.error422.item.ErrorReportItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, Error422Mod.MODID);

    public static final RegistryObject<Item> GLITCH_FRAGMENT = ITEMS.register("glitch_fragment",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> CORRUPTED_DATA = ITEMS.register("corrupted_data",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<ErrorReportItem> ERROR_REPORT = ITEMS.register("error_report",
            () -> new ErrorReportItem(new Item.Properties().stacksTo(8)));

    public static final RegistryObject<ErrorCoreItem> ERROR_CORE = ITEMS.register("error_core",
            () -> new ErrorCoreItem(new Item.Properties().stacksTo(1)));

    private ModItems() {
    }
}
