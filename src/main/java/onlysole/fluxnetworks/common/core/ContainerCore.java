package onlysole.fluxnetworks.common.core;

import onlysole.fluxnetworks.api.network.INetworkConnector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerCore extends Container {

    public INetworkConnector connector;

    public ContainerCore(EntityPlayer player, INetworkConnector tileEntity) {
        this.connector = tileEntity;
        this.connector.open(player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        connector.close(playerIn);
    }
}
