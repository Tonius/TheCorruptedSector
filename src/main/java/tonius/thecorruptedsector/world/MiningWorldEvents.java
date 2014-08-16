package tonius.thecorruptedsector.world;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;

public class MiningWorldEvents {

    @SubscribeEvent
    public void tryTeleportToOverworld(PlayerTickEvent evt) {
        if (evt.side == Side.SERVER) {
            if (evt.player.dimension == 15 && evt.player.posY < -2.5D) {
                EntityPlayerMP playerMP = (EntityPlayerMP) evt.player;
                WorldServer overworld = playerMP.mcServer.worldServerForDimension(0);
                if (overworld != null) {
                    playerMP.fallDistance = 0.0F;
                    playerMP.mountEntity(null);
                    if (playerMP.riddenByEntity != null) {
                        playerMP.riddenByEntity.mountEntity(null);
                    }
                    playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, 0, new TeleporterBasic(overworld));
                }
            }
        }
    }

    @SubscribeEvent
    public void replaceBiomeBlocks(ReplaceBiomeBlocks evt) {
        if (evt.chunkProvider instanceof ChunkProviderMining) {
            Random random = ((ChunkProviderMining) evt.chunkProvider).getRNG();
            if (random.nextInt(192) == 0) {
                evt.setResult(Result.DENY);
                for (int i = 0; i < evt.blockArray.length; i++)
                    evt.blockArray[i] = null;
            } else if (random.nextInt(50) == 0) {
                evt.setResult(Result.DENY);
            }
        }
    }

}
