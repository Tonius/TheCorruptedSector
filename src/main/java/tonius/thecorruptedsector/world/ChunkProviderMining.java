package tonius.thecorruptedsector.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkProviderGenerate;
import tonius.thecorruptedsector.config.TCSConfig;

public class ChunkProviderMining extends ChunkProviderGenerate {
    
    private Random rand;
    
    public ChunkProviderMining(World world) {
        super(world, world.getSeed() + TCSConfig.seedOffset, world.getWorldInfo().isMapFeaturesEnabled());
        this.rand = new Random(world.getSeed() + TCSConfig.seedOffset);
    }
    
    public Random getRNG() {
        return this.rand;
    }
    
    @Override
    public void func_147424_a(int chunkX, int chunkZ, Block[] blocks) {
        if (TCSConfig.scrambledChunks && this.rand.nextInt(TCSConfig.scrambledChunkRarity) == 0) {
            super.func_147424_a(chunkX + this.rand.nextInt(16) - 8, chunkZ + this.rand.nextInt(16) - 8, blocks);
            return;
        }
        
        super.func_147424_a(chunkX, chunkZ, blocks);
    }
    
}
