package com.onlysole.fluxnetworks.api.utils;

import com.onlysole.fluxnetworks.api.network.ISuperAdmin;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {

    @CapabilityInject(ISuperAdmin.class)
    public static Capability<ISuperAdmin> SUPER_ADMIN = null;
}
