package com.jtop.sotm.client.sky;

import com.jtop.sotm.moon.MoonPhase;
import net.minecraft.resources.ResourceLocation;

public class MoonTextureManager {

    public static final ResourceLocation MOON_BLUE_MOON =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_1_blue_moon.png");
    public static final ResourceLocation MOON_TRISTE =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_3_triste.png");
    public static final ResourceLocation MOON_BLOOD_MOON =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_4_blood_moon.png");
    public static final ResourceLocation MOON_DESAPARICION =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "textures/moon/phase_5_desaparicion.png");

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
            case TRISTE -> MOON_TRISTE;
            case BLOOD_MOON -> MOON_BLOOD_MOON;
            case DESAPARICION -> MOON_DESAPARICION;
            default -> VANILLA_MOON;
        };
    }
}