package tonius.thecorruptedsector.world;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterBasic extends Teleporter {

    private final WorldServer worldServer;

    public TeleporterBasic(WorldServer worldServer) {
        super(worldServer);
        this.worldServer = worldServer;
    }

    @Override
    public void placeInPortal(Entity entity, double x, double y, double z, float somethingWithYaw) {
        Material mat;
        ChunkCoordinates dest = new ChunkCoordinates((int) x, entity.worldObj.getActualHeight() + 2, (int) z);
        do {
            mat = this.worldServer.provider.worldObj.getBlock(dest.posX, dest.posY, dest.posZ).getMaterial();
            if (dest.posY <= 0 || mat != null && (mat.isSolid() || mat.isLiquid())) {
                break;
            }
            dest.posY--;
        } while (mat == null || (!mat.isSolid() && !mat.isLiquid()));
        entity.setPosition(dest.posX + 0.5D, dest.posY + 2.0D, dest.posZ + 0.5D);
    }

}
