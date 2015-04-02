package tonius.thecorruptedsector.util;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import tonius.thecorruptedsector.TheCorruptedSector;
import tonius.thecorruptedsector.block.TileEntityMiningPortal;
import tonius.thecorruptedsector.config.TCSConfig;
import cpw.mods.fml.common.FMLCommonHandler;

public class TeleportUtils {
    
    public static boolean teleportPlayerToPos(EntityPlayerMP player, int dimension, int posX, int posY, int posZ, boolean simulate) {
        if (!DimensionManager.isDimensionRegistered(dimension)) {
            return false;
        }
        
        if (!simulate) {
            player.mountEntity(null);
            if (player.riddenByEntity != null) {
                player.riddenByEntity.mountEntity(null);
            }
            if (player.dimension != dimension) {
                teleportPlayerToDimension(player, dimension, player.mcServer.getConfigurationManager());
            }
            player.setPositionAndUpdate(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
        }
        
        return true;
    }
    
    public static void teleportPlayerToMiningWorld(EntityPlayerMP player, boolean backToOverworld, boolean spawnPortalBlock) {
        int toDim = backToOverworld ? 0 : TCSConfig.dimensionID;
        ChunkCoordinates coords = new ChunkCoordinates((int) player.posX, (int) player.posY, (int) player.posZ);
        World world = player.mcServer.worldServerForDimension(toDim);
        coords = findOrCreatePortal(world, coords, spawnPortalBlock);
        
        player.mountEntity(null);
        if (player.riddenByEntity != null) {
            player.riddenByEntity.mountEntity(null);
        }
        teleportPlayerToPos(player, toDim, coords.posX, coords.posY, coords.posZ, false);
    }
    
    public static ChunkCoordinates findOrCreatePortal(World world, ChunkCoordinates coords, boolean spawnPortalBlock) {
        ChunkCoordinates closest = null;
        float closestDistance = Float.MAX_VALUE;
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                Map<ChunkPosition, TileEntity> tileEntityMap = world.getChunkFromChunkCoords((coords.posX >> 4) + i, (coords.posZ >> 4) + j).chunkTileEntityMap;
                for (ChunkPosition tileCoords : tileEntityMap.keySet()) {
                    TileEntity tile = tileEntityMap.get(tileCoords);
                    if (tile instanceof TileEntityMiningPortal) {
                        ChunkCoordinates teleportCoords = ((TileEntityMiningPortal) tile).getFreePosition();
                        if (teleportCoords != null) {
                            float distance = coords.getDistanceSquaredToChunkCoordinates(teleportCoords);
                            if (distance < closestDistance) {
                                closest = teleportCoords;
                                closestDistance = distance;
                            }
                        }
                    }
                }
            }
        }
        if (closest != null) {
            return closest;
        }
        
        coords.posY = world.getActualHeight();
        Material mat;
        do {
            mat = world.getBlock(coords.posX, coords.posY, coords.posZ).getMaterial();
            if (mat.isSolid() || mat.isLiquid() || coords.posY <= 2) {
                if (spawnPortalBlock || !World.doesBlockHaveSolidTopSurface(world, coords.posX, coords.posY, coords.posZ)) {
                    world.setBlock(coords.posX, coords.posY + 1, coords.posZ, spawnPortalBlock ? TheCorruptedSector.miningPortal : Blocks.cobblestone);
                }
                break;
            }
            coords.posY--;
        } while (!mat.isSolid() && !mat.isLiquid());
        coords.posY += 2;
        
        return coords;
    }
    
    public static void teleportEntityToWorld(Entity entity, WorldServer oldWorld, WorldServer newWorld) {
        WorldProvider pOld = oldWorld.provider;
        WorldProvider pNew = newWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double x = entity.posX * moveFactor;
        double z = entity.posZ * moveFactor;
        
        oldWorld.theProfiler.startSection("placing");
        x = MathHelper.clamp_double(x, -29999872, 29999872);
        z = MathHelper.clamp_double(z, -29999872, 29999872);
        
        if (entity.isEntityAlive()) {
            entity.setLocationAndAngles(x, entity.posY, z, entity.rotationYaw, entity.rotationPitch);
            newWorld.spawnEntityInWorld(entity);
            newWorld.updateEntityWithOptionalForce(entity, false);
        }
        
        oldWorld.theProfiler.endSection();
        
        entity.setWorld(newWorld);
    }
    
    public static void teleportPlayerToDimension(EntityPlayerMP player, int dimension, ServerConfigurationManager manager) {
        int oldDim = player.dimension;
        WorldServer worldserver = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.dimension = dimension;
        WorldServer worldserver1 = manager.getServerInstance().worldServerForDimension(player.dimension);
        player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
        worldserver.removePlayerEntityDangerously(player);
        player.isDead = false;
        teleportEntityToWorld(player, worldserver, worldserver1);
        manager.func_72375_a(player, worldserver);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(worldserver1);
        manager.updateTimeAndWeatherForPlayer(player, worldserver1);
        manager.syncPlayerInventory(player);
        Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
        
        while (iterator.hasNext()) {
            PotionEffect potioneffect = iterator.next();
            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), potioneffect));
        }
        FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, oldDim, dimension);
    }
    
}
