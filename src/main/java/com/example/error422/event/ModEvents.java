package com.example.error422.event;

import com.example.error422.Error422Mod;
import com.example.error422.entity.Error422Entity;
import com.example.error422.registry.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Error422Mod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEvents {

    private ModEvents() {
    }

    @SubscribeEvent
    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ERROR_422.get(), Error422Entity.createAttributes().build());
    }
}
