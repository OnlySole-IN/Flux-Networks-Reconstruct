package onlysole.fluxnetworks.common.integration.energy;

import onlysole.fluxnetworks.api.energy.IItemEnergyHandler;
import onlysole.fluxnetworks.api.energy.ITileEnergyHandler;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergyTile;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class IC2EnergyHandler implements ITileEnergyHandler, IItemEnergyHandler {

    public static final IC2EnergyHandler INSTANCE = new IC2EnergyHandler();

    private IC2EnergyHandler() {
    }

    @Override
    public boolean hasCapability(@Nonnull TileEntity tile, EnumFacing side) {
        return tile instanceof IEnergyTile || tile instanceof IEnergyStorage;
    }

    @Override
    public boolean canAddEnergy(@Nonnull TileEntity tile, EnumFacing side) {
        return tile instanceof IEnergySink || tile instanceof IEnergyStorage;
    }

    @Override
    public boolean canRemoveEnergy(@Nonnull TileEntity tile, EnumFacing side) {
        return false;
    }

    @Override
    public long addEnergy(long amount, @Nonnull TileEntity tile, EnumFacing side, boolean simulate) {
        if (tile instanceof IEnergyStorage) {
            IEnergyStorage sink = (IEnergyStorage) tile;
            int before = sink.getStored();
            if (!simulate) {
                return (long) sink.addEnergy((int) Math.min(sink.getCapacity() - before, amount >> 2)) << 2;
            } else {
                return Math.min(sink.getCapacity() - before, amount >> 2) << 2;
            }
        } else if (tile instanceof IEnergySink) {
            IEnergySink sink = (IEnergySink) tile;
            double voltage = EnergyNet.instance.getPowerFromTier(sink.getSinkTier());
            double a = Math.min(amount >> 2, voltage);
            if (simulate) {
                return (long) Math.min(a, sink.getDemandedEnergy()) << 2;
            } else {
                return (long) Math.floor(a - sink.injectEnergy(side, a, voltage)) << 2;
            }
        }
        return 0;
    }

    @Override
    public long removeEnergy(long amount, @Nonnull TileEntity tile, EnumFacing side) {
        return 0;
    }

    @Override
    public boolean hasCapability(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof IElectricItem || stack.getItem() instanceof ISpecialElectricItem);
    }

    @Override
    public boolean canAddEnergy(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof IElectricItem || stack.getItem() instanceof ISpecialElectricItem);
    }

    @Override
    public boolean canRemoveEnergy(@Nonnull ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof IElectricItem || stack.getItem() instanceof ISpecialElectricItem);
    }

    @Override
    public long addEnergy(long amount, @Nonnull ItemStack stack, boolean simulate) {
        IElectricItemManager manager = getManager(stack);
        return (long) manager.charge(stack, amount >> 2, 4, false, simulate) << 2;
    }

    @Override
    public long removeEnergy(long amount, @Nonnull ItemStack stack) {
        IElectricItemManager manager = getManager(stack);
        return (long) manager.discharge(stack, amount >> 2, 4, false, true, false) << 2;
    }

    public static IElectricItemManager getManager(ItemStack stack) {
        if (stack.getItem() instanceof ISpecialElectricItem) {
            IElectricItemManager manager = ((ISpecialElectricItem) stack.getItem()).getManager(stack);
            if (manager == null) {
                manager = ElectricItem.getBackupManager(stack);
            }
            return manager;
        }
        return ElectricItem.manager;
    }
}
