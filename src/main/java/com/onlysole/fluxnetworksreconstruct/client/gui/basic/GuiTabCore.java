package com.onlysole.fluxnetworksreconstruct.client.gui.basic;

import com.onlysole.fluxnetworksreconstruct.FluxConfig;
import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.api.gui.EnumFeedbackInfo;
import com.onlysole.fluxnetworksreconstruct.api.gui.EnumNavigationTabs;
import com.onlysole.fluxnetworksreconstruct.api.network.INetworkConnector;
import com.onlysole.fluxnetworksreconstruct.client.gui.GuiFluxAdminHome;
import com.onlysole.fluxnetworksreconstruct.client.gui.GuiFluxConfiguratorHome;
import com.onlysole.fluxnetworksreconstruct.client.gui.GuiFluxConnectorHome;
import com.onlysole.fluxnetworksreconstruct.client.gui.button.NavigationButton;
import com.onlysole.fluxnetworksreconstruct.client.gui.tab.*;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemAdminConfigurator;
import com.onlysole.fluxnetworksreconstruct.common.item.ItemConfigurator;
import com.onlysole.fluxnetworksreconstruct.common.registry.RegistrySounds;
import com.onlysole.fluxnetworksreconstruct.common.tileentity.TileFluxCore;
import com.google.common.collect.Lists;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.IOException;
import java.util.List;

/**for guis which have navigation tabs along the top */
public abstract class GuiTabCore extends GuiFluxCore {

    protected List<NavigationButton> navigationButtons = Lists.newArrayList();
    public EnumNavigationTabs[] navigationTabs;

    public GuiTabCore(EntityPlayer player, INetworkConnector connector) {
        super(player, connector);
        setDefaultTabs();
    }

    @Override
    public void initGui() {
        super.initGui();
        navigationButtons.clear();
        buttonLists.add(navigationButtons);
    }

    public abstract EnumNavigationTabs getNavigationTab();

    public void setDefaultTabs(){
        this.navigationTabs = EnumNavigationTabs.values();
    }

    public void setNavigationTabs(EnumNavigationTabs[] navigationTabs){
        this.navigationTabs = navigationTabs;
    }

    @Override
    protected void keyTypedMain(char c, int k) throws IOException {
        super.keyTypedMain(c, k);
        if (k == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(k)) {
            if(textBoxes.stream().noneMatch(GuiTextField::isFocused)) {
                if(this.getNavigationTab() == EnumNavigationTabs.TAB_HOME){
                    mc.player.closeScreen();
                }else {
                    switchTab(EnumNavigationTabs.TAB_HOME, player, connector);
                    FluxNetworksReconstruct.proxy.setFeedback(EnumFeedbackInfo.NONE, false);
                    if (FluxConfig.enableButtonSound)
                        mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RegistrySounds.BUTTON_CLICK, 1.0F));
                }
            }
        }
    }


    public void onButtonClicked(GuiButtonCore button, int mouseX, int mouseY, int mouseButton){
        if(mouseButton == 0 && button instanceof NavigationButton){
            switchTab(((NavigationButton)button).tab, player, connector);
            if (FluxConfig.enableButtonSound)
                mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(RegistrySounds.BUTTON_CLICK, 1.0F));
        }
    }

    public void configureNavigationButtons(EnumNavigationTabs currentTab, EnumNavigationTabs[] availableTabs){
        int posCount = 0;
        for(EnumNavigationTabs tab : availableTabs){
            if(tab != EnumNavigationTabs.TAB_CREATE){
                navigationButtons.add(new NavigationButton(12 + (18 * posCount), -16, tab));
                posCount++;
            }else {
                navigationButtons.add(new NavigationButton(148, -16, tab));
            }
        }
        navigationButtons.get(currentTab.ordinal()).setMain();
    }

    public static void switchTab(EnumNavigationTabs tab, EntityPlayer player, INetworkConnector connector) {
        switch (tab) {
            case TAB_HOME:
                if(connector instanceof TileFluxCore) {
                    FMLCommonHandler.instance().showGuiScreen(new GuiFluxConnectorHome(player, (TileFluxCore) connector));
                } else if(connector instanceof ItemAdminConfigurator.AdminConnector) {
                    FMLCommonHandler.instance().showGuiScreen(new GuiFluxAdminHome(player, connector));
                } else if(connector instanceof ItemConfigurator.NetworkConnector) {
                    FMLCommonHandler.instance().showGuiScreen(new GuiFluxConfiguratorHome(player, (ItemConfigurator.NetworkConnector)connector));
                }else {
                    player.closeScreen();
                }
                break;
            case TAB_SELECTION:
                if(connector instanceof ItemAdminConfigurator.AdminConnector && FluxNetworksReconstruct.proxy.detailed_network_view) {
                    FMLCommonHandler.instance().showGuiScreen(new GuiTabDetailedSelection(player, connector));
                    break;
                }
                FMLCommonHandler.instance().showGuiScreen(new GuiTabSelection(player, connector));
                break;
            case TAB_WIRELESS:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabWireless(player, connector));
                break;
            case TAB_CONNECTION:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabConnections(player, connector));
                break;
            case TAB_STATISTICS:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabStatistics(player, connector));
                break;
            case TAB_MEMBER:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabMembers(player, connector));
                break;
            case TAB_SETTING:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabSettings(player, connector));
                break;
            case TAB_CREATE:
                FMLCommonHandler.instance().showGuiScreen(new GuiTabCreate(player, connector));
                break;
        }

    }
}
