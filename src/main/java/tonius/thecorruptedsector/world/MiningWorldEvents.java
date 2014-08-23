package tonius.thecorruptedsector.world;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.terraingen.ChunkProviderEvent.ReplaceBiomeBlocks;
import tonius.thecorruptedsector.util.TeleportUtils;
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
                TeleportUtils.teleportPlayerToMiningWorld(playerMP, true, false);
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
