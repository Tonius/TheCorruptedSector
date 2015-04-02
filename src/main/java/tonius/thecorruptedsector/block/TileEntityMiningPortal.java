package tonius.thecorruptedsector.block;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class TileEntityMiningPortal extends TileEntity {
    
    public ChunkCoordinates getFreePosition() {
        Material mat;
        Material mat2;
        boolean platform;
        for (int y = -2; y <= 2; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && z == 0 && y == 0) {
                        continue;
                    }
                    mat = this.worldObj.getBlock(this.xCoord + x, this.yCoord + y, this.zCoord + z).getMaterial();
                    mat2 = this.worldObj.getBlock(this.xCoord + x, this.yCoord + y + 1, this.zCoord + z).getMaterial();
                    platform = World.doesBlockHaveSolidTopSurface(this.worldObj, this.xCoord + x, this.yCoord + y - 1, this.zCoord + z);
                    if (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid() && platform) {
                        return new ChunkCoordinates(this.xCoord + x, this.yCoord + y, this.zCoord + z);
                    }
                }
            }
        }
        return null;
    }
    
}
