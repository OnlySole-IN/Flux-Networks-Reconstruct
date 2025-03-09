package com.onlysole.fluxnetworksreconstruct;

import com.onlysole.fluxnetworksreconstruct.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = FluxNetworks.MODID, name = FluxNetworks.NAME, version = FluxNetworks.VERSION, dependencies = "required-after:forge@[14.23.4.2854,)", acceptedMinecraftVersions = "[1.12.2]", guiFactory = "com.example.fluxnetworks.common.core.ConfigGuiFactory")
public class FluxNetworks {

    public static final String MODID = "fluxnetworks";
    public static final String NAME = "Flux Networks";
    public static final String VERSION = "4.1.0";

    @Mod.Instance(MODID)
    public static FluxNetworks instance;

    public static Logger logger = LogManager.getLogger("FluxNetworks");

    @SidedProxy(clientSide = "com.example.fluxnetworks.client.ClientProxy", serverSide = "com.example.fluxnetworks.common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void init(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        proxy.onServerStarted();
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        proxy.onServerStopped();
    }
}
