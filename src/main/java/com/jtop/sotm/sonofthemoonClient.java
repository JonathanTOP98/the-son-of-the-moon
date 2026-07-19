package com.jtop.sotm;

import com.jtop.sotm.client.sky.MoonTextureManager;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = sonofthemoon.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = sonofthemoon.MODID, value = Dist.CLIENT)
public class sonofthemoonClient {
    public sonofthemoonClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        sonofthemoon.LOGGER.info("HELLO FROM CLIENT SETUP");
        sonofthemoon.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        MoonTextureManager.init();
        MoonSkyRenderer.init();
    }
}