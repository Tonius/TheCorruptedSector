package tonius.thecorruptedsector;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.apache.logging.log4j.Logger;

import tonius.thecorruptedsector.block.BlockMiningPortal;
import tonius.thecorruptedsector.block.TileEntityMiningPortal;
import tonius.thecorruptedsector.config.TCSConfig;
import tonius.thecorruptedsector.world.MiningWorldEvents;
import tonius.thecorruptedsector.world.WorldProviderMining;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "thecorruptedsector", guiFactory = "tonius.thecorruptedsector.config.ConfigGuiFactoryTCS")
public class TheCorruptedSector {

    @Instance("thecorruptedsector")
    public static TheCorruptedSector instance;

    public static Logger logger;

    public static Block miningPortal = null;

    @EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting The Corrupted Sector");

        TCSConfig.preInit(evt);

        logger.info("Registering blocks");
        miningPortal = new BlockMiningPortal();
        GameRegistry.registerBlock(miningPortal, "miningPortal");
        GameRegistry.registerTileEntity(TileEntityMiningPortal.class, "TheCorruptedSector.miningPortal");
    }

    @EventHandler
    public void init(FMLInitializationEvent evt) {
        logger.info("Registering dimension");
        DimensionManager.registerProviderType(TCSConfig.dimensionID, WorldProviderMining.class, false);
        DimensionManager.registerDimension(TCSConfig.dimensionID, TCSConfig.dimensionID);
        FMLCommonHandler.instance().bus().register(new MiningWorldEvents());
        MinecraftForge.EVENT_BUS.register(new MiningWorldEvents());
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent evt) {
        logger.info("Registering recipes");
        GameRegistry.addRecipe(new ShapedOreRecipe(miningPortal, new Object[] { "IRG", "LPL", "GRI", 'I', "ingotIron", 'G', "ingotGold", 'R', "dustRedstone", 'L', new ItemStack(Items.dye, 1, 4), 'P', Items.ender_pearl }).setMirrored(true));
        GameRegistry.addRecipe(new ShapedOreRecipe(miningPortal, new Object[] { "ILG", "RPR", "GLI", 'I', "ingotIron", 'G', "ingotGold", 'R', "dustRedstone", 'L', new ItemStack(Items.dye, 1, 4), 'P', Items.ender_pearl }).setMirrored(true));
    }

}
