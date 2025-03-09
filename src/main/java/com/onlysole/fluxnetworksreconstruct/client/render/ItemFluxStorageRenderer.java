package com.onlysole.fluxnetworksreconstruct.client.render;

import com.onlysole.fluxnetworksreconstruct.client.FluxColorHandler;
import com.onlysole.fluxnetworksreconstruct.common.block.BlockFluxStorage;
import com.onlysole.fluxnetworksreconstruct.common.core.FluxUtils;
import com.onlysole.fluxnetworksreconstruct.common.data.FluxNetworkData;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemFluxStorageRenderer extends TileEntityItemStackRenderer {

    public static final ItemFluxStorageRenderer INSTANCE = new ItemFluxStorageRenderer();

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = blockrendererdispatcher.getModelForState(Block.getBlockFromItem(stack.getItem()).getDefaultState());
        GlStateManager.translate(0.5, 0.5, 0.5);
        net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, FluxStorageModel.CURRENT_TRANSFORM, true);
        Minecraft.getMinecraft().getRenderItem().renderItem(stack, model);
        GlStateManager.translate(-0.5, -0.5, -0.5);

        BlockFluxStorage block = (BlockFluxStorage) Block.getBlockFromItem(stack.getItem());
        NBTTagCompound tag = stack.getSubCompound(FluxUtils.FLUX_DATA);
        if(tag != null) {
            int colour = FluxColorHandler.getOrRequestNetworkColor(tag.getInteger(FluxNetworkData.NETWORK_ID));
            TileFluxStorageRenderer.render(tag.getLong("energy"), block.getMaxStorage(), colour, 0.0D, 0.0D, 0.0D);
        }
    }
}
