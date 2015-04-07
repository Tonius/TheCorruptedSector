package tonius.thecorruptedsector.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import tonius.thecorruptedsector.TheCorruptedSector;
import tonius.thecorruptedsector.world.WorldProviderMining.DayNightCycleMode;
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TCSConfig {
    
    public static Configuration config;
    public static List<ConfigSection> configSections = new ArrayList<ConfigSection>();
    
    public static final ConfigSection sectionWorldProps = new ConfigSection("World Properties", "worldProps");
    public static final ConfigSection sectionWorldCorruptness = new ConfigSection("World Corruptness", "worldCorruptness");
    
    // worldProps default
    public static final int dimensionID_default = 13;
    public static final int seedOffset_default = 1;
    public static final DayNightCycleMode dayNightCycleMode_default = DayNightCycleMode.NORMAL;
    public static final boolean clouds_default = false;
    
    // worldCorruptness default
    public static final boolean worldHoles_default = true;
    public static final int worldHoleRarity_default = 192;
    public static final boolean barrenChunks_default = true;
    public static final int barrenChunkRarity_default = 64;
    public static final boolean scrambledChunks_default = true;
    public static final int scrambledChunkRarity_default = 3;
    
    // worldProps
    public static int dimensionID = dimensionID_default;
    public static int seedOffset = seedOffset_default;
    public static DayNightCycleMode dayNightCycleMode = dayNightCycleMode_default;
    public static boolean clouds = clouds_default;
    
    // worldCorruptness
    public static boolean worldHoles = worldHoles_default;
    public static int worldHoleRarity = worldHoleRarity_default;
    public static boolean barrenChunks = barrenChunks_default;
    public static int barrenChunkRarity = barrenChunkRarity_default;
    public static boolean scrambledChunks = scrambledChunks_default;
    public static int scrambledChunkRarity = scrambledChunkRarity_default;
    
    public static void preInit(FMLPreInitializationEvent evt) {
        FMLCommonHandler.instance().bus().register(new TCSConfig());
        config = new Configuration(evt.getSuggestedConfigurationFile());
        TheCorruptedSector.logger.info("Loading configuration file");
        syncConfig();
    }
    
    public static void syncConfig() {
        try {
            processConfig();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            config.save();
        }
    }
    
    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent evt) {
        if (evt.modID.equals("thecorruptedsector")) {
            TheCorruptedSector.logger.info("Updating configuration file");
            syncConfig();
        }
    }
    
    public static void processConfig() {
        dimensionID = config.get(sectionWorldProps.name, "Dimension ID", dimensionID_default, "The dimension ID of the Corrupted Sector.").getInt(dimensionID_default);
        seedOffset = config.get(sectionWorldProps.name, "Seed Offset", seedOffset_default, "This value will be added to the seed of the world to get the seed of the Corrupted Sector. Setting this to 0 will cause the Corrupted Sector to have the same seed as the Overworld. Change this if you are resetting the Corrupted Sector, and don't want the exact same world as before.").getInt(seedOffset_default);
        dayNightCycleMode = DayNightCycleMode.values()[config.get(sectionWorldProps.name, "Day/Night Cycle Mode", dayNightCycleMode_default.ordinal(), "0 = normal day/night cycle, 1 = eternal day, 2 = eternal night").setMinValue(0).setMaxValue(2).getInt(dayNightCycleMode_default.ordinal())];
        clouds = config.get(sectionWorldProps.name, "Clouds", clouds_default, "Whether the Corrupted Sector has clouds.").getBoolean(clouds_default);
        
        worldHoles = config.get(sectionWorldCorruptness.name, "World Holes", worldHoles_default, "If enabled, holes to the void will generate. These can be jumped into to teleport back to the Overworld as a last resort.").getBoolean(worldHoles_default);
        worldHoleRarity = config.get(sectionWorldCorruptness.name, "World Hole Rarity", worldHoleRarity_default, "The rarity of world holes, if they are enabled. Every 1 in [rarity] chunks will be a world hole.").setMinValue(1).getInt(worldHoleRarity_default);
        barrenChunks = config.get(sectionWorldCorruptness.name, "Barren Chunks", barrenChunks_default, "If enabled, barren chunks will generate. Barren chunks are chunks without any grass, trees, etc.").getBoolean(barrenChunks_default);
        barrenChunkRarity = config.get(sectionWorldCorruptness.name, "Barren Chunk Rarity", barrenChunkRarity_default, "The rarity of barren chunks, if they are enabled. Every 1 in [rarity] chunks will be a barren chunk.").setMinValue(1).getInt(barrenChunkRarity_default);
        scrambledChunks = config.get(sectionWorldCorruptness.name, "Scrambled Chunks", scrambledChunks_default, "If enabled, some chunks will be scrambled around with nearby chunks, causing visible chunk edges. This is the most obvious corruptness effect.").getBoolean(scrambledChunks_default);
        scrambledChunkRarity = config.get(sectionWorldCorruptness.name, "Scrambled Chunk Rarity", scrambledChunkRarity_default, "The rarity of scrambled chunks, if they are enabled. Every 1 in [rarity] chunks will be a scrambled chunk.").setMinValue(1).getInt(scrambledChunkRarity_default);
    }
    
    public static class ConfigSection {
        
        public String name;
        public String id;
        
        public ConfigSection(String name, String id) {
            this.name = name;
            this.id = id;
            TCSConfig.configSections.add(this);
        }
        
        public String toLowerCase() {
            return this.name.toLowerCase();
        }
        
    }
    
}
