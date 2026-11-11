package com.example.error422.registry;

import com.example.error422.Error422Mod;
import com.example.error422.entity.Error422Entity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Error422Mod.MODID);

    public static final RegistryObject<EntityType<Error422Entity>> ERROR_422 =
            ENTITY_TYPES.register("error422", () -> EntityType.Builder
                    .of(Error422Entity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.9F)
                    .clientTrackingRange(12)
                    .build("error422"));

    private ModEntities() {
    }
}
