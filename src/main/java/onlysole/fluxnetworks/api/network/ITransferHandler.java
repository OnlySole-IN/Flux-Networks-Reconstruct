package onlysole.fluxnetworks.api.network;

import onlysole.fluxnetworks.api.utils.NBTType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public interface ITransferHandler {

    void onCycleStart();

    void onCycleEnd();

    long getBuffer();

    long getRequest();

    /**
     * Get the energy change produced by externals in last tick.
     * For instance, a Plug may receive energy, but not transmit them across the network,
     * so energy change is the amount it received rather than zero, they just went to the buffer.
     * If a Point is requesting 1EU but we can only provide 3FE, the 3FE will go to the
     * Point buffer, and the energy change of the Point is zero rather than 3.
     *
     * @return energy change
     */
    long getChange();

    void addToBuffer(long amount);

    long removeFromBuffer(long amount);

    long receiveFromSupplier(long amount, @Nonnull EnumFacing side, boolean simulate);

    void writeCustomNBT(NBTTagCompound tag, NBTType type);

    void readCustomNBT(NBTTagCompound tag, NBTType type);

    void updateTransfers(EnumFacing... faces);

    void reset();
}
