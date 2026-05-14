package com.github.dedinc.demodisabler;

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
    private static boolean disabled = false;

    public DemoDisabler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onInitGuiPre(ScreenEvent.Init.Pre event) {
        if (disabled) return;
        if (event.getScreen() instanceof TitleScreen) {
            disableDemoMode();
        }
    }

    private void disableDemoMode() {
        try {
            Minecraft mc = Minecraft.getInstance();

            Field demoField = Minecraft.class.getDeclaredField("f_90980_");
            demoField.setAccessible(true);
            demoField.set(mc, false);

            Field allowsMultiplayerField = Minecraft.class.getDeclaredField("f_90978_");
            allowsMultiplayerField.setAccessible(true);
            allowsMultiplayerField.set(mc, true);

            Field allowsChatField = Minecraft.class.getDeclaredField("f_90979_");
            allowsChatField.setAccessible(true);
            allowsChatField.set(mc, true);

            LOGGER.info("Demo mode disabled successfully.");
            disabled = true;
        } catch (Exception e) {
            LOGGER.error("Failed to disable demo mode: ", e);
        }
    }
}
