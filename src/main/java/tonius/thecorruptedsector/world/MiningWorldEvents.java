package tonius.thecorruptedsector.world;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import tonius.thecorruptedsector.config.TCSConfig;
import tonius.thecorruptedsector.util.TeleportUtils;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;

public class MiningWorldEvents {

    @SubscribeEvent
    public void tryTeleportToOverworld(PlayerTickEvent evt) {
        if (evt.side == Side.SERVER) {
            if (evt.player.dimension == TCSConfig.dimensionID && evt.player.posY < -2.5D) {
                EntityPlayerMP playerMP = (EntityPlayerMP) evt.player;
                playerMP.fallDistance = 0.0F;
                TeleportUtils.teleportPlayerToMiningWorld(playerMP, true, false);
            }
        }
    }

    @SubscribeEvent
    public void replaceBiomeBlocks(ReplaceBiomeBlocks evt) {
        if (evt.chunkProvider instanceof ChunkProviderMining) {
            Random random = ((ChunkProviderMining) evt.chunkProvider).getRNG();
            if (TCSConfig.worldHoles && random.nextInt(TCSConfig.worldHoleRarity) == 0) {
                evt.setResult(Result.DENY);
                for (int i = 0; i < evt.blockArray.length; i++)
                    evt.blockArray[i] = null;
            } else if (TCSConfig.barrenChunks && random.nextInt(TCSConfig.barrenChunkRarity) == 0) {
                evt.setResult(Result.DENY);
            }
        }
    }

}
