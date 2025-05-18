package com.onlysole.fluxnetworks.common.tileentity.energy;

import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Optional;

@Optional.InterfaceList({
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySink", modid = "ic2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergySource", modid = "ic2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyAcceptor", modid = "ic2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyEmitter", modid = "ic2"),
        @Optional.Interface(iface = "ic2.api.energy.tile.IEnergyTile", modid = "ic2")
})
public abstract class TileIC2Energy extends TileRedstoneFlux implements IEnergySink, IEnergySource {

    boolean IC2Connected = false;

    @Override
    @Optional.Method(modid = "ic2")
    public void onLoad() {
        super.onLoad();
        if (!this.getWorld().isRemote && !IC2Connected) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
            IC2Connected = true;
        }
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void invalidate() {
        super.invalidate();
        if (!this.getWorld().isRemote && IC2Connected) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            IC2Connected = false;
        }
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void onChunkUnload() {
        super.onChunkUnload();
        if (!this.getWorld().isRemote && IC2Connected) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
            IC2Connected = false;
        }
    }

    @Override
    @Optional.Method(modid = "ic2")
    public double getDemandedEnergy() {
        return Integer.MAX_VALUE;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public int getSinkTier() {
        return 5;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
        return getConnectionType().isPlug();
    }

    @Override
    @Optional.Method(modid = "ic2")
    public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
        if (getConnectionType().isPlug()) {
            return amount - getTransferHandler().receiveFromSupplier((long) amount * 4, directionFrom.getOpposite(), false) / 4D;
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
        return getConnectionType().isPoint();
    }

    @Override
    @Optional.Method(modid = "ic2")
    public double getOfferedEnergy() {
        return 0;
    }

    @Override
    @Optional.Method(modid = "ic2")
    public void drawEnergy(double amount) {
    }

    @Override
    @Optional.Method(modid = "ic2")
    public int getSourceTier() {
        return 5;
    }

}
