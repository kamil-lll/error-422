package com.example.error422.registry;

import com.example.error422.Error422Mod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Error422Mod.MODID);

    public static final RegistryObject<CreativeModeTab> ERROR_TAB = TABS.register("error422",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.error422"))
                    .icon(() -> new ItemStack(ModItems.ERROR_CORE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.GLITCH_FRAGMENT.get());
                        output.accept(ModItems.CORRUPTED_DATA.get());
                        output.accept(ModItems.ERROR_REPORT.get());
                        output.accept(ModItems.ERROR_CORE.get());
                    })
                    .build());

    private ModTabs() {
    }
}
