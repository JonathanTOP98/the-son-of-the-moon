package com.jtop.sotm.network;

import com.jtop.sotm.moon.MoonPhase;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record MoonPhasePayload(int phaseId) implements CustomPacketPayload {

    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath("sonofthemoon", "moon_phase");

    public static final Type<MoonPhasePayload> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, MoonPhasePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    MoonPhasePayload::phaseId,
                    MoonPhasePayload::new
            );

    public MoonPhasePayload(MoonPhase phase) {
        this(phase.getId());
    }

    public MoonPhase getPhase() {
        return MoonPhase.byId(phaseId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}