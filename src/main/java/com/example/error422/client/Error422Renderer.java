package com.example.error422.client;

import com.example.error422.Error422Mod;
import com.example.error422.entity.Error422Entity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class Error422Renderer extends MobRenderer<Error422Entity, HumanoidModel<Error422Entity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            Error422Mod.MODID, "textures/entity/error422.png");

    public Error422Renderer(EntityRendererProvider.Context context) {
        super(context, new HeadlessHumanoidModel(context.bakeLayer(ClientEvents.LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(Error422Entity entity) {
        return TEXTURE;
    }
}
