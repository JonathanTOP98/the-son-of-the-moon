package com.jtop.sotm.client.sky;

import com.jtop.sotm.moon.MoonPhase;
import net.minecraft.resources.ResourceLocation;

public class MoonTextureManager {

    public static final ResourceLocation MOON_BLUE_MOON =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_1_blue_moon.png");
    public static final ResourceLocation MOON_SAD =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_3_sad.png");
    public static final ResourceLocation MOON_BLOOD_MOON =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_4_blood_moon.png");
    public static final ResourceLocation MOON_TRANSPARENT =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/transparent.png");

    public static final ResourceLocation VANILLA_MOON =
            ResourceLocation.withDefaultNamespace("textures/environment/moon_phases.png");

    private static MoonPhase currentPhase = MoonPhase.NORMAL;

    public static void init() {
    }

    public static void setPhase(MoonPhase phase) {
        currentPhase = phase;
    }

    public static MoonPhase getCurrentPhase() {
        return currentPhase;
    }

    public static ResourceLocation getCurrentMoonTexture() {
        return switch (currentPhase) {
            case BLUE_MOON -> MOON_BLUE_MOON;
            case SAD -> MOON_SAD;
            case BLOOD_MOON -> MOON_BLOOD_MOON;
            default -> VANILLA_MOON;
        };
    }
}