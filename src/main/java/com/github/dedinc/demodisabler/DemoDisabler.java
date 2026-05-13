package com.github.dedinc.demodisabler;

import com.mojang.authlib.yggdrasil.YggdrasilSocialInteractionsService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
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

    public DemoDisabler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInitGuiPost(ScreenEvent.Init.Post event) {
        if (disabled) return;

        // In 1.20.1, MainMenuScreen is now TitleScreen
        if (event.getScreen() instanceof TitleScreen) {
            disableDemoMode();
        }
    }

    private void disableDemoMode() {
        try {
            setPrivateField(Minecraft.class, mc, "f_91040_", false);
            setPrivateField(Minecraft.class, mc, "f_91045_", true);
            setPrivateField(Minecraft.class, mc, "f_91046_", true);

            Field serviceField = Minecraft.class.getDeclaredField("f_91041_");
            serviceField.setAccessible(true);
            YggdrasilSocialInteractionsService service = (YggdrasilSocialInteractionsService) serviceField.get(mc);

            if (service != null) {
                setPrivateField(YggdrasilSocialInteractionsService.class, service, "serversAllowed", true);
                setPrivateField(YggdrasilSocialInteractionsService.class, service, "chatAllowed", true);
            }

            LOGGER.info("Demo mode has been disabled via 1.20.1 Reflection.");
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
}
