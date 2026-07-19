package com.jtop.sotm.mixin;

import com.jtop.sotm.client.sky.MoonTextureManager;
import com.jtop.sotm.moon.MoonPhase;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    private static final ResourceLocation VANILLA_MOON_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");

    @Redirect(
            method = "renderSky",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"
            )
    )
    private void sonofthemoon$replaceMoonTexture(int sampler, ResourceLocation location) {
        if (location.equals(VANILLA_MOON_LOCATION)) {
            MoonPhase phase = MoonTextureManager.getCurrentPhase();

            if (phase == MoonPhase.MISSING) {
                RenderSystem.setShaderTexture(sampler, MoonTextureManager.MOON_TRANSPARENT);
                return;
            }

            if (phase != MoonPhase.NORMAL && phase != MoonPhase.CALM) {
                RenderSystem.setShaderTexture(sampler, MoonTextureManager.MOON_TRANSPARENT);
                return;
            }
        }
        RenderSystem.setShaderTexture(sampler, location);
    }
}