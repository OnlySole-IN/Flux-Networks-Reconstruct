package onlysole.fluxnetworks.common.network;

import onlysole.fluxnetworks.FluxConfig;
import onlysole.fluxnetworks.api.gui.EnumFeedbackInfo;
import onlysole.fluxnetworks.api.network.FluxLogicType;
import onlysole.fluxnetworks.api.network.IFluxNetwork;
import onlysole.fluxnetworks.api.network.NetworkSettings;
import onlysole.fluxnetworks.common.connection.FluxNetworkCache;
import onlysole.fluxnetworks.common.data.FluxChunkManager;
import onlysole.fluxnetworks.common.data.FluxNetworkData;
import onlysole.fluxnetworks.common.tileentity.TileFluxCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketTileHandler {

    public static NBTTagCompound getSetNetworkPacket(int id, String password) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger(FluxNetworkData.NETWORK_ID, id);
        tag.setString(FluxNetworkData.NETWORK_PASSWORD, password);
        return tag;
    }

    public static IMessage handleSetNetworkPacket(TileFluxCore tile, EntityPlayer player, NBTTagCompound tag) {
        int id = tag.getInteger(FluxNetworkData.NETWORK_ID);
        String pass = tag.getString(FluxNetworkData.NETWORK_PASSWORD);
        if(tile.getNetworkID() == id) {
            return null;
        }
        IFluxNetwork network = FluxNetworkCache.instance.getNetwork(id);
        if(!network.isInvalid()) {
            if(tile.getConnectionType().isController() && network.getConnections(FluxLogicType.CONTROLLER).size() > 0) {
                return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.HAS_CONTROLLER);
            }
            if(!network.getMemberPermission(player).canAccess()) {
                if(pass.isEmpty()) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.PASSWORD_REQUIRE);
                }
                if (!pass.equals(network.getSetting(NetworkSettings.NETWORK_PASSWORD))) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.REJECT);
                }
            }
            if(tile.getNetwork() != null && !tile.getNetwork().isInvalid()) {
                tile.getNetwork().queueConnectionRemoval(tile, false);
            }
            tile.playerUUID = EntityPlayer.getUUID(player.getGameProfile());
            network.queueConnectionAddition(tile);
            return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.SUCCESS);
        }
        return null;
    }

    public static NBTTagCompound getChunkLoadPacket(boolean chunkLoading) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("c", chunkLoading);
        return tag;
    }

    public static IMessage handleChunkLoadPacket(TileFluxCore tile, EntityPlayer player, NBTTagCompound tag) {
        boolean load = tag.getBoolean("c");
        if(FluxConfig.general.enableChunkLoading) {
            if (load) {
                boolean p = FluxChunkManager.forceChunk(tile.getWorld(), new ChunkPos(tile.getPos()));
                tile.chunkLoading = p;
                if(!p) {
                    return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.HAS_LOADER);
                }
                return null;
            } else {
                FluxChunkManager.releaseChunk(tile.getWorld(), new ChunkPos(tile.getPos()));
                tile.chunkLoading = false;
                return null;
            }
        } else {
            tile.chunkLoading = false;
        }
        return new PacketFeedback.FeedbackMessage(EnumFeedbackInfo.BANNED_LOADING);
    }
}
