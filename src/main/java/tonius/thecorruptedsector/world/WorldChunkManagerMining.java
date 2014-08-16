package tonius.thecorruptedsector.world;

import net.minecraft.world.World;
import net.minecraft.world.biome.WorldChunkManager;

public class WorldChunkManagerMining extends WorldChunkManager {

    public WorldChunkManagerMining(World world) {
        super(world.getSeed() + 1, world.getWorldInfo().getTerrainType());
    }

}
