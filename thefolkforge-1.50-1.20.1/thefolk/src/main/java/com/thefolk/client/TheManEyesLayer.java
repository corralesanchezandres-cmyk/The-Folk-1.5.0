package com.thefolk.client;

import com.thefolk.TheFolkMod;
import com.thefolk.entity.TheManEntity;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

/** Ojos blancos que brillan. */
public class TheManEyesLayer extends EyesLayer<TheManEntity, HumanoidModel<TheManEntity>> {
    private static final RenderType EYES = RenderType.eyes(
            new ResourceLocation(TheFolkMod.MOD_ID, "textures/entity/the_man_eyes.png"));

    public TheManEyesLayer(RenderLayerParent<TheManEntity, HumanoidModel<TheManEntity>> parent) {
        super(parent);
    }

    @Override
    public RenderType renderType() {
        return EYES;
    }
}
