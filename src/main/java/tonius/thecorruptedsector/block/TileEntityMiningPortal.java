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
                    if (x == 0 && z == 0 && y == 0)
                        continue;
                    mat = worldObj.getBlock(xCoord + x, yCoord + y, zCoord + z).getMaterial();
                    mat2 = worldObj.getBlock(xCoord + x, yCoord + y + 1, zCoord + z).getMaterial();
                    platform = World.doesBlockHaveSolidTopSurface(worldObj, xCoord + x, yCoord + y - 1, zCoord + z);
                    if (!mat.isSolid() && !mat.isLiquid() && !mat2.isSolid() && !mat2.isLiquid() && platform)
                        return new ChunkCoordinates(xCoord + x, yCoord + y, zCoord + z);
                }
            }
        }
        return null;
    }

}
