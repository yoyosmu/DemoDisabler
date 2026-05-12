package com.github.dedinc.demodisabler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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
            setPrivateField(Minecraft.class, mc, "f_91040_", false);           
            setPrivateField(Minecraft.class, mc, "f_91045_", true);           
            setPrivateField(Minecraft.class, mc, "f_91046_", true);           

            Object service = getPrivateField(Minecraft.class, mc, "f_91041_");
            if (service != null) {
                setPrivateField(service.getClass(), service, "serversAllowed", true);
                setPrivateField(service.getClass(), service, "chatAllowed", true);
            }

            LOGGER.info("Demo mode bypassed for 1.20.1 using legacy Reflection.");
            disabled = true;
            
            mc.setScreen(new TitleScreen());
        } catch (Exception e) {
            LOGGER.error("CRITICAL: Failed to find obfuscated fields in 1.20.1: ", e);
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
