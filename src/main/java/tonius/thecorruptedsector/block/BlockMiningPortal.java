package tonius.thecorruptedsector.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tonius.thecorruptedsector.TheCorruptedSector;
import tonius.thecorruptedsector.config.TCSConfig;
import tonius.thecorruptedsector.util.TeleportUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockMiningPortal extends Block implements ITileEntityProvider {
    
    public BlockMiningPortal() {
        super(Material.rock);
        this.setBlockName("thecorruptedsector.miningPortal");
        this.setBlockTextureName("thecorruptedsector:miningPortal");
        this.setHardness(2.0F);
        this.setResistance(2.0F);
        this.setLightLevel(0.5F);
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float offsetX, float offsetY, float offsetZ) {
        if (player.dimension == 0 || player.dimension == TCSConfig.dimensionID) {
            if (!world.isRemote) {
                TeleportUtils.teleportPlayerToMiningWorld((EntityPlayerMP) player, player.dimension == TCSConfig.dimensionID, true);
            }
            return true;
        }
        return false;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        if (rand.nextInt(3) == 0) {
            world.playSound(x + 0.5D, y + 0.5D, z + 0.5D, "mob.endermen.portal", 0.01F + rand.nextFloat() * 0.3F, 0.8F + rand.nextFloat(), false);
        }
        double particleX, particleY, particleZ;
        for (int i = 1; i < rand.nextInt(5) + 2; i++) {
            particleX = x + 0.5D + (rand.nextDouble() * 1.2D - 0.6D);
            particleY = y + 0.5D + (rand.nextDouble() * 1.2D - 0.6D);
            particleZ = z + 0.5D + (rand.nextDouble() * 1.2D - 0.6D);
            world.spawnParticle("blockcrack_" + Block.getIdFromBlock(TheCorruptedSector.miningPortal) + "_0", particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMiningPortal();
    }
    
}
