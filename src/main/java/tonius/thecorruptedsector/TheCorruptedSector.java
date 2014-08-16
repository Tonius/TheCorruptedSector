package tonius.thecorruptedsector;

import net.minecraft.item.Item;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Logger;

import tonius.thecorruptedsector.item.ItemMiningTest;
import tonius.thecorruptedsector.world.MiningWorldEvents;
import tonius.thecorruptedsector.world.WorldProviderMining;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "thecorruptedsector", name = "The Corrupted Sector")
public class TheCorruptedSector {

    @Instance("thecorruptedsector")
    public static TheCorruptedSector instance;

    public static Logger log;

    public static Item miningTest = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        log = evt.getModLog();

        miningTest = new ItemMiningTest();
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        GameRegistry.registerItem(miningTest, "miningTest");

        DimensionManager.registerProviderType(15, WorldProviderMining.class, false);
        DimensionManager.registerDimension(15, 15);
        FMLCommonHandler.instance().bus().register(new MiningWorldEvents());
        MinecraftForge.EVENT_BUS.register(new MiningWorldEvents());
    }

}
