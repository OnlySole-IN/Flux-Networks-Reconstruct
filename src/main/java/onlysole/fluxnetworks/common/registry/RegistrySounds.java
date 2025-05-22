package onlysole.fluxnetworks.common.registry;

import onlysole.fluxnetworks.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class RegistrySounds {

    public static SoundEvent BUTTON_CLICK;

    public static void registerSounds(IForgeRegistry<SoundEvent> registry) {
        BUTTON_CLICK = registerSound(registry, "button");
    }

    public static SoundEvent registerSound(IForgeRegistry<SoundEvent> registry, String soundName) {
        ResourceLocation soundID = new ResourceLocation(Tags.MOD_ID, soundName);
        SoundEvent event = new SoundEvent(soundID).setRegistryName(soundID);
        registry.register(event);
        return event;
    }
}
