package com.onlysole.fluxnetworks.client.gui;

import com.onlysole.fluxnetworks.FluxConfig;
import com.onlysole.fluxnetworks.Tags;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiModConfig extends GuiConfig {

    public GuiModConfig(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), Tags.MOD_ID, false, false, Tags.MOD_NAME);
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.GENERAL)));
        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.CLIENT)));
        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.NETWORKS)));
        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.ENERGY)));
        list.add(new ConfigElement(FluxConfig.config.getCategory(FluxConfig.BLACKLIST)));
        return list;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        FluxConfig.config.save();
        FluxConfig.read();
        FluxConfig.verifyAndReadBlacklist();
    }
}
