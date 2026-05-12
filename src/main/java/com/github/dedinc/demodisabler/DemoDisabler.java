package com.github.dedinc.demodisabler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("demodisablermod")
public class DemoDisabler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean disabled = false;

    public DemoDisabler() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        modBus.addListener(this::onAddLayers);
    }

    public void onAddLayers(AddGuiOverlayLayersEvent event) {
        // Modern API to hide the "minecraft:demo" overlay [4, 5]
        event.addConditionTo(ResourceLocation.withDefaultNamespace("demo"), () -> !disabled);
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

            var service = mc.socialInteractionsService;
            if (service != null) {
                service.serversAllowed = true;
                service.chatAllowed = true;
            }

            LOGGER.info("Demo mode disabled.");
            disabled = true;
            mc.setScreen(new TitleScreen());
        } catch (Exception e) {
            LOGGER.error("Failed to disable demo mode: ", e);
        }
    }
}
