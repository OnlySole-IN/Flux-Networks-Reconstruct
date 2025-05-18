package com.onlysole.fluxnetworks.client.gui.tab;

import com.onlysole.fluxnetworks.api.gui.EnumNavigationTabs;
import com.onlysole.fluxnetworks.api.network.INetworkConnector;
import com.onlysole.fluxnetworks.api.network.NetworkSettings;
import com.onlysole.fluxnetworks.api.translate.FluxTranslate;
import com.onlysole.fluxnetworks.api.utils.NBTType;
import com.onlysole.fluxnetworks.client.gui.LineChart;
import com.onlysole.fluxnetworks.client.gui.basic.GuiTabCore;
import com.onlysole.fluxnetworks.common.connection.NetworkStatistics;
import com.onlysole.fluxnetworks.common.core.FluxUtils;
import com.onlysole.fluxnetworks.common.handler.PacketHandler;
import com.onlysole.fluxnetworks.common.network.PacketNetworkUpdateRequest;
import com.onlysole.fluxnetworks.common.registry.RegistryBlocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class GuiTabStatistics extends GuiTabCore {

    private NetworkStatistics stats = network.getSetting(NetworkSettings.NETWORK_STATISTICS);
    private LineChart chart;
    private int timer = 0;
    private static NBTTagCompound GUI_COLOR_TAG;
    private static ItemStack plug;
    private static ItemStack point;
    private static ItemStack storage;
    private static ItemStack controller;

    static {
        GUI_COLOR_TAG = new NBTTagCompound();
        GUI_COLOR_TAG.setBoolean(FluxUtils.GUI_COLOR, true);
        plug = new ItemStack(RegistryBlocks.FLUX_PLUG);
        plug.setTagCompound(GUI_COLOR_TAG);
        point = new ItemStack(RegistryBlocks.FLUX_POINT);
        point.setTagCompound(GUI_COLOR_TAG);
        storage = new ItemStack(RegistryBlocks.FLUX_STORAGE_1);
        storage.setTagCompound(GUI_COLOR_TAG);
        controller = new ItemStack(RegistryBlocks.FLUX_CONTROLLER);
        controller.setTagCompound(GUI_COLOR_TAG);
    }

    public GuiTabStatistics(EntityPlayer player, INetworkConnector connector) {
        super(player, connector);
    }

    public EnumNavigationTabs getNavigationTab() {
        return EnumNavigationTabs.TAB_STATISTICS;
    }

    @Override
    protected void drawForegroundLayer(int mouseX, int mouseY) {
        super.drawForegroundLayer(mouseX, mouseY);
        if (networkValid) {
            int color = network.getSetting(NetworkSettings.NETWORK_COLOR);
            renderNetwork(network.getSetting(NetworkSettings.NETWORK_NAME), color, 20, 8);

            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.PLUGS.t() + TextFormatting.GRAY + ": " +
                    TextFormatting.RESET + stats.fluxPlugCount, 12, 24, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.POINTS.t() + TextFormatting.GRAY + ": " +
                    TextFormatting.RESET + stats.fluxPointCount, 12, 36, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.STORAGES.t() + TextFormatting.GRAY + ": " +
                    TextFormatting.RESET + stats.fluxStorageCount, 82, 24, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.CONTROLLERS.t() + TextFormatting.GRAY + ": " +
                    TextFormatting.RESET + stats.fluxControllerCount, 82, 36, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.INPUT.t() + TextFormatting.GRAY + ": " + TextFormatting.RESET +
                    FluxUtils.format(stats.energyInput, FluxUtils.TypeNumberFormat.COMPACT, network.getSetting(NetworkSettings.NETWORK_ENERGY), true), 12, 48, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.OUTPUT.t() + TextFormatting.GRAY + ": " + TextFormatting.RESET +
                    FluxUtils.format(stats.energyOutput, FluxUtils.TypeNumberFormat.COMPACT, network.getSetting(NetworkSettings.NETWORK_ENERGY), true), 12, 60, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.BUFFER.t() + TextFormatting.GRAY + ": " + TextFormatting.RESET +
                    FluxUtils.format(stats.totalBuffer, FluxUtils.TypeNumberFormat.COMPACT, network.getSetting(NetworkSettings.NETWORK_ENERGY), false), 12, 72, color);
            fontRenderer.drawString(TextFormatting.GRAY + FluxTranslate.ENERGY.t() + TextFormatting.GRAY + ": " + TextFormatting.RESET +
                    FluxUtils.format(stats.totalEnergy, FluxUtils.TypeNumberFormat.COMPACT, network.getSetting(NetworkSettings.NETWORK_ENERGY), false), 12, 84, color);

            GlStateManager.scale(0.75, 0.75, 0.75);
            drawCenteredString(fontRenderer, FluxTranslate.AVERAGE_TICK.t() + ": " + stats.averageTickMicro + " \u00b5s/t", (int) ((xSize / 2) * (1 / 0.75)), (int) ((ySize - 2) * (1 / 0.75)), color);
            GlStateManager.scale(1 / 0.75, 1 / 0.75, 1 / 0.75);

        } else {
            renderNavigationPrompt(FluxTranslate.ERROR_NO_SELECTED.t(), FluxTranslate.TAB_SELECTION.t());
        }
    }

    @Override
    protected void drawBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawBackgroundLayer(partialTicks, mouseX, mouseY);
        if (networkValid && chart != null) {
            chart.drawChart(mc);
            chart.updateHeight(partialTicks);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        configureNavigationButtons(EnumNavigationTabs.TAB_STATISTICS, navigationTabs);

        if (networkValid) {
            chart = new LineChart(width / 2 - 48, height / 2 + 20, 50, 6, "s", "RF");
            chart.updateData(stats.energyChange);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (networkValid) {
            if (timer == 0) {
                PacketHandler.network.sendToServer(new PacketNetworkUpdateRequest.UpdateRequestMessage(network.getNetworkID(), NBTType.NETWORK_STATISTICS));
            }
            if (timer == 1) {
                chart.updateData(stats.energyChange);
            }
            timer++;
            timer %= 20;
        }
    }
}
