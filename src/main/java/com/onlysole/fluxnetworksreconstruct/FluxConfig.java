package com.onlysole.fluxnetworksreconstruct;

import com.onlysole.fluxnetworksreconstruct.common.handler.ItemEnergyHandler;
import com.onlysole.fluxnetworksreconstruct.common.handler.TileEntityHandler;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class FluxConfig {

    public static Configuration config;

    public static final String GENERAL = "general";
    public static final String CLIENT = "client";
    public static final String ENERGY = "energy";
    public static final String NETWORKS = "networks";
    public static final String BLACKLIST = "blacklists";

    public static boolean enableButtonSound, enableOneProbeBasicInfo, enableOneProbeAdvancedInfo, enableOneProbeSneaking;
    public static boolean enableFluxRecipe, enableOldRecipe, enableChunkLoading, enableSuperAdmin;
    public static int defaultLimit, basicCapacity, basicTransfer, herculeanCapacity, herculeanTransfer, gargantuanCapacity, gargantuanTransfer;
    public static int maximumPerPlayer, superAdminRequiredPermission;
    public static String[] blockBlacklistStrings, itemBlackListStrings;

    public static void init(File file) {
        config = new Configuration(new File(file.getPath(), "flux_networks.cfg"));
        config.load();
        read();
        verifyAndReadBlacklist();
        config.save();
        generateFluxChunkConfig();
    }

    public static void verifyAndReadBlacklist() {
        TileEntityHandler.blockBlacklist.clear();
        for (String str : blockBlacklistStrings) {
            if (!str.contains(":")) {
                FluxNetworksReconstruct.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }
            String root = str;
            int meta = -1;
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]);
                    TileEntityHandler.blockBlacklist.put(root, meta);
                } catch (Exception e) {
                    FluxNetworksReconstruct.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                TileEntityHandler.blockBlacklist.put(root, meta);
            }
        }
        ItemEnergyHandler.itemBlackList.clear();
        for (String str : itemBlackListStrings) {
            if (!str.contains(":")) {
                FluxNetworksReconstruct.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, please use 'modid:name@meta'");
            }
            String root = str;
            int meta = -1;
            if (str.contains("@")) {
                String[] split = str.split("@");
                root = split[0];
                try {
                    meta = Integer.parseInt(split[1]);
                    ItemEnergyHandler.itemBlackList.put(root, meta);
                } catch (Exception e) {
                    FluxNetworksReconstruct.logger.error("BLACKLIST ERROR: " + str + " has incorrect formatting, meta must be positive integer'");
                }
            } else {
                ItemEnergyHandler.itemBlackList.put(root, meta);
            }
        }
    }

    public static void generateFluxChunkConfig() {
        if (!ForgeChunkManager.getConfig().hasCategory(FluxNetworksReconstruct.MODID)) {
            ForgeChunkManager.getConfig().get(FluxNetworksReconstruct.MODID, "maximumChunksPerTicket", 1000000).setMinValue(0);
            ForgeChunkManager.getConfig().get(FluxNetworksReconstruct.MODID, "maximumTicketCount", 1000000).setMinValue(0);
            ForgeChunkManager.getConfig().save();
        }
    }

    public static void read() {
        defaultLimit = config.getInt("Default Transfer Limit", ENERGY, 800000, 0, Integer.MAX_VALUE, "The default transfer limit of a flux connector");

        basicCapacity = config.getInt("Basic Storage Capacity", ENERGY, 1000000, 0, Integer.MAX_VALUE, "");
        basicTransfer = config.getInt("Basic Storage Transfer", ENERGY, 20000, 0, Integer.MAX_VALUE, "");
        herculeanCapacity = config.getInt("Herculean Storage Capacity", ENERGY, 8000000, 0, Integer.MAX_VALUE, "");
        herculeanTransfer = config.getInt("Herculean Storage Transfer", ENERGY, 120000, 0, Integer.MAX_VALUE, "");
        gargantuanCapacity = config.getInt("Gargantuan Storage Capacity", ENERGY, 128000000, 0, Integer.MAX_VALUE, "");
        gargantuanTransfer = config.getInt("Gargantuan Storage Transfer", ENERGY, 1440000, 0, Integer.MAX_VALUE, "");

        maximumPerPlayer = config.getInt("Maximum Networks Per Player", NETWORKS, 3, -1, Integer.MAX_VALUE, "Maximum networks each player can have. -1 = no limit");
        enableSuperAdmin = config.getBoolean("Allow Network Super Admin", NETWORKS, true, "Allows someone to be a network super admin, otherwise, no one can access or dismantle your flux devices or delete your networks without permission");
        superAdminRequiredPermission = config.getInt("Permission level required to activate Super Admin", NETWORKS, 1, 0, Integer.MAX_VALUE, "See ops.json. If the player has permission level equal or greater to the value set here they will be able to Activate Super Admin. Setting this to 0 will allow anyone to active Super Admin.");

        enableFluxRecipe = config.getBoolean("Enable Flux Recipe", GENERAL, true, "Enables redstones being compressed with the bedrock and obsidian to get flux");
        enableOldRecipe = config.getBoolean("Enable Old Recipe", GENERAL, false, "Enables redstone being turned into Flux when dropped in fire. (Need \"Enable Flux Recipe\" = true, so the default recipe can't be disabled if turns this on)");
        enableChunkLoading = config.getBoolean("Allow Flux Chunk Loading", GENERAL, true, "Allows flux tiles to work as chunk loaders");

        enableButtonSound = config.getBoolean("Enable GUI Button Sound", CLIENT, true, "Enable navigation buttons sound when pressing it");
        enableOneProbeBasicInfo = config.getBoolean("Enable Basic One Probe Info", CLIENT, true, "Displays: Network Name, Live Transfer Rate & Internal Buffer");
        enableOneProbeAdvancedInfo = config.getBoolean("Enable Advanced One Probe Info", CLIENT, true, "Displays: Transfer Limit & Priority etc");
        enableOneProbeSneaking = config.getBoolean("Enable sneaking to display Advanced One Probe Info", CLIENT, true, "Displays Advanced Info when sneaking only");

        blockBlacklistStrings = getBlackList("Block Connection Blacklist", BLACKLIST, new String[]{"actuallyadditions:block_phantom_energyface"}, "a blacklist for blocks which flux connections shouldn't connect to, use format 'modid:name@meta'");
        itemBlackListStrings = getBlackList("Item Transfer Blacklist", BLACKLIST, new String[]{}, "a blacklist for items which the Flux Controller shouldn't transfer to, use format 'modid:name@meta'");
    }

    public static String[] getBlackList(String name, String category, String[] defaultValue, String comment) {
        Property prop = config.get(category, name, defaultValue);
        prop.setLanguageKey(name);
        prop.setValidValues(null);
        prop.setComment(comment);
        return prop.getStringList();
    }
}
