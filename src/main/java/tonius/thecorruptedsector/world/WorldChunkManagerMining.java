package tonius.thecorruptedsector.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;
import tonius.thecorruptedsector.config.TCSConfig;

public class WorldChunkManagerMining extends WorldChunkManager {

    public WorldChunkManagerMining(World world) {
        super(world.getSeed() + TCSConfig.seedOffset, world.getWorldInfo().getTerrainType());
    }

}
