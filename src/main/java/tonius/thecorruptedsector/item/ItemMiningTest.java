package tonius.thecorruptedsector.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tonius.thecorruptedsector.world.TeleporterBasic;

public class ItemMiningTest extends Item {

    // TODO portal block because that's safer

    public ItemMiningTest() {
        this.setMaxStackSize(1);
        this.setUnlocalizedName("tonisutilities.miningTest");
        this.setTextureName("tonisutilities:miningTest");
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if (entityplayer instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entityplayer;
            if (player.dimension != 15) {
                player.timeUntilPortal = 5;
                player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 15, new TeleporterBasic(player.mcServer.worldServerForDimension(15)));
            } else {
                player.timeUntilPortal = 5;
                player.mcServer.getConfigurationManager().transferPlayerToDimension(player, 0, new TeleporterBasic(player.mcServer.worldServerForDimension(0)));
            }
        }
        return itemstack;
    }

}
