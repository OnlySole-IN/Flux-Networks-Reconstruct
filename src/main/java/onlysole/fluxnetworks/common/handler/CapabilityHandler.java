package onlysole.fluxnetworks.common.handler;

import onlysole.fluxnetworks.Tags;
import onlysole.fluxnetworks.common.capabilities.CapabilitySAProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

    private static final ResourceLocation SUPER_ADMIN = new ResourceLocation(Tags.MOD_ID, "SuperAdmin");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(SUPER_ADMIN, new CapabilitySAProvider());
        }
    }
}
