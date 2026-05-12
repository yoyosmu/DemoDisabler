package com.github.dedinc.demodisabler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@Mod("demodisablermod")
public class DemoDisabler {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Minecraft mc = Minecraft.getInstance();
    private static boolean disabled = false;
    private static final ResourceLocation DEMO_OVERLAY = new ResourceLocation("minecraft", "demo");

    public DemoDisabler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (DEMO_OVERLAY.equals(event.getOverlay().id())) {
            event.setCanceled(true);
        }
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
            setPrivateField(Minecraft.class, mc, "demo", false);
            setPrivateField(Minecraft.class, mc, "allowsMultiplayer", true);
            setPrivateField(Minecraft.class, mc, "allowsChat", true);

            Object service = getPrivateField(Minecraft.class, mc, "socialInteractionsService");
            if (service != null) {
                setPrivateField(service.getClass(), service, "serversAllowed", true);
                setPrivateField(service.getClass(), service, "chatAllowed", true);
            }

            LOGGER.info("Demo mode disabled for 1.20.1 via Reflection.");
            disabled = true;
            mc.setScreen(new TitleScreen());
        } catch (Exception e) {
            LOGGER.error("Failed to disable demo mode: ", e);
        }
    }

    private void setPrivateField(Class<?> clazz, Object instance, String fieldName, Object value) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    private Object getPrivateField(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
}
