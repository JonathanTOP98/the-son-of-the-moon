package com.jtop.sotm.event;

import com.jtop.sotm.data.MoonSavedData;
import com.jtop.sotm.network.MoonPhasePayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "sonofthemoon")
public class PlayerConnectionEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        MoonSavedData data = MoonSavedData.get(player.serverLevel());
        PacketDistributor.sendToPlayer(player, new MoonPhasePayload(data.getPhase()));
    }
}