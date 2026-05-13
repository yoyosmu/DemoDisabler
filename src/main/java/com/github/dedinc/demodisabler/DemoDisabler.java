package com.github.dedinc.demodisabler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("demodisablermod")
public class DemoDisabler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean disabled = false;

    public DemoDisabler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInitGuiPost(ScreenEvent.Init.Post event) {
        if (disabled) return;
        if (event.getScreen() instanceof TitleScreen) {
            disableDemoMode();
        }
    }

    private void disableDemoMode() {
        try {
            mc.demo = false;
            mc.allowsMultiplayer = true;
            mc.allowsChat = true;

            if (mc.socialInteractionsService != null) { 
                mc.socialInteractionsService.serversAllowed = true;
                mc.socialInteractionsService.chatAllowed = true;
            }

            LOGGER.info("Demo mode successfully bypassed via Access Transformers.");
            disabled = true;
            
            mc.setScreen(new TitleScreen());
        } catch (Exception e) {
            LOGGER.error("Failed to disable demo mode: ", e);
        }
    }
}
