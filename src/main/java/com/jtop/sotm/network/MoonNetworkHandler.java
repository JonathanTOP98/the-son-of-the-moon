package com.jtop.sotm.network;

import com.jtop.sotm.client.sky.MoonTextureManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class MoonNetworkHandler {

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(MoonNetworkHandler::onRegisterPayloads);
    }

    private static void onRegisterPayloads(final RegisterPayloadHandlersEvent event) {
        event.registrar("1.0")
                .playToClient(
                        MoonPhasePayload.TYPE,
                        MoonPhasePayload.STREAM_CODEC,
                        new DirectionalPayloadHandler<>(
                                MoonNetworkHandler::handleClientPayload,
                                (payload, context) -> {}
                        )
                );
    }

    private static void handleClientPayload(final MoonPhasePayload payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            MoonTextureManager.setPhase(payload.getPhase());
        });
    }
}