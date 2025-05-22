package onlysole.fluxnetworks.common.capabilities;

import onlysole.fluxnetworks.api.network.ISuperAdmin;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class DefaultSAStorage implements Capability.IStorage<ISuperAdmin> {

    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISuperAdmin> capability, ISuperAdmin instance, EnumFacing side) {
        return instance.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void readNBT(Capability<ISuperAdmin> capability, ISuperAdmin instance, EnumFacing side, NBTBase nbt) {
        instance.readFromNBT((NBTTagCompound) nbt);
    }
}
