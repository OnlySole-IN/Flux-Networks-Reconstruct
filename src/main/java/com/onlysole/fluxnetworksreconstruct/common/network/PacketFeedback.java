package com.onlysole.fluxnetworksreconstruct.common.network;

import com.onlysole.fluxnetworksreconstruct.FluxNetworksReconstruct;
import com.onlysole.fluxnetworksreconstruct.api.gui.EnumFeedbackInfo;
import com.onlysole.fluxnetworksreconstruct.common.handler.PacketHandler;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketFeedback implements IMessageHandler<PacketFeedback.FeedbackMessage, IMessage> {

    @Override
    public IMessage onMessage(FeedbackMessage message, MessageContext ctx) {
        PacketHandler.handlePacket(() -> {
            if(message.info == EnumFeedbackInfo.SUCCESS || message.info == EnumFeedbackInfo.SUCCESS_2 || message.info == EnumFeedbackInfo.PASSWORD_REQUIRE) {
                FluxNetworksReconstruct.proxy.setFeedback(message.info, true);
            } else {
                FluxNetworksReconstruct.proxy.setFeedback(message.info, false);
            }
            }, ctx.netHandler);
        return null;
    }

    public static class FeedbackMessage implements IMessage {

        public EnumFeedbackInfo info;

        public FeedbackMessage() {
        }

        public FeedbackMessage(EnumFeedbackInfo info) {
            this.info = info;
        }

        @Override
        public void fromBytes(ByteBuf buf) {
            info = EnumFeedbackInfo.values()[buf.readInt()];
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(info.ordinal());
        }
    }
}
