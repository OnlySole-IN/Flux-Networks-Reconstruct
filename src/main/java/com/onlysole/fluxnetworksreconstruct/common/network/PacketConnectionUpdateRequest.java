package com.onlysole.fluxnetworksreconstruct.common.network;

import com.onlysole.fluxnetworksreconstruct.api.network.FluxLogicType;
import com.onlysole.fluxnetworksreconstruct.api.network.IFluxNetwork;
import com.onlysole.fluxnetworksreconstruct.api.tiles.IFluxConnector;
import com.onlysole.fluxnetworksreconstruct.api.utils.Coord4D;
import com.onlysole.fluxnetworksreconstruct.common.connection.FluxLiteConnector;
import com.onlysole.fluxnetworksreconstruct.common.connection.FluxNetworkCache;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.List;

public class PacketConnectionUpdateRequest implements IMessageHandler<PacketConnectionUpdateRequest.ConnectionRequestMessage, IMessage> {

    @Override
    public IMessage onMessage(ConnectionRequestMessage message, MessageContext ctx) {
        if(message.coords.isEmpty()) {
            return null;
        }
        IFluxNetwork network = FluxNetworkCache.instance.getNetwork(message.networkID);
        if(!network.isInvalid()) {
            List<NBTTagCompound> tags = new ArrayList<>();
            //noinspection unchecked
            List<IFluxConnector> onlineConnectors = network.getConnections(FluxLogicType.ANY);
            message.coords.forEach(c ->
                    onlineConnectors.stream().filter(f -> f.getCoords().equals(c)).findFirst()
                            .ifPresent(f -> tags.add(FluxLiteConnector.writeCustomNBT(f, new NBTTagCompound()))));
            return new PacketConnectionUpdate.NetworkConnectionMessage(message.networkID, tags);
        }
        return null;
    }

    public static class ConnectionRequestMessage implements IMessage {

        public int networkID;
        public List<Coord4D> coords = new ArrayList<>();

        public ConnectionRequestMessage() {

        }

        public ConnectionRequestMessage(int networkID, List<Coord4D> coords) {
            this.networkID = networkID;
            this.coords = coords;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            networkID = buf.readInt();
            int size = buf.readInt();
            for(int i = 0; i < size; i++) {
                coords.add(new Coord4D(buf));
            }
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(networkID);
            buf.writeInt(coords.size());
            coords.forEach(c -> c.write(buf));
        }
    }
}
