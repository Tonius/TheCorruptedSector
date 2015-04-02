package tonius.thecorruptedsector.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.ForgeHooksClient;
import tonius.thecorruptedsector.config.TCSConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class WorldProviderMining extends WorldProvider {
    
    @Override
    protected void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerMining(this.worldObj);
    }
    
    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderMining(this.worldObj);
    }
    
    @Override
    public boolean canRespawnHere() {
        return false;
    }
    
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int temp;
        switch (TCSConfig.dayNightCycleMode) {
        default:
        case NORMAL:
            return super.calculateCelestialAngle(worldTime, partialTicks);
        case ETERNAL_DAY:
            temp = (int) (worldTime % 24000L);
            return (float) (Math.cos((temp + partialTicks) * (10F / 60000F) * Math.PI) * 0.2F);
        case ETERNAL_NIGHT:
            temp = (int) (worldTime % 24000L);
            return (float) (Math.cos((temp + partialTicks) * (10F / 60000F) * Math.PI) * 0.2F + 0.5F);
        }
    }
    
    @Override
    public int getMoonPhase(long worldTime) {
        return 0;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public float getCloudHeight() {
        if (!TCSConfig.clouds) {
            return Float.MAX_VALUE;
        }
        return super.getCloudHeight();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(Entity cameraEntity, float partialTicks) {
        float celestialAngle = this.worldObj.getCelestialAngle(partialTicks);
        float daylight = (float) (Math.cos(celestialAngle * Math.PI * 2.0F) * 2.0F + 0.5F);
        
        if (daylight < 0.1F) {
            daylight = 0.1F;
        }
        
        if (daylight > 1.0F) {
            daylight = 1.0F;
        }
        
        int posX = (int) Math.floor(cameraEntity.posX);
        int posY = (int) Math.floor(cameraEntity.posY);
        int posZ = (int) Math.floor(cameraEntity.posZ);
        
        int skyBlendColor = ForgeHooksClient.getSkyBlendColour(this.worldObj, posX, posY, posZ);
        
        float skyRed = (skyBlendColor >> 16 & 255) / 255.0F;
        float skyGreen = (skyBlendColor >> 8 & 255) / 255.0F;
        float skyBlue = (skyBlendColor & 255) / 255.0F;
        
        skyRed *= daylight;
        skyGreen *= daylight;
        skyBlue *= daylight;
        
        float rainStrength = this.worldObj.getRainStrength(partialTicks);
        float temp1, temp2;
        
        if (rainStrength > 0.0F) {
            temp1 = (skyRed * 0.3F + skyGreen * 0.59F + skyBlue * 0.11F) * 0.6F;
            temp2 = 1.0F - rainStrength * 0.75F;
            skyRed = skyRed * temp2 + temp1 * (1.0F - temp2);
            skyGreen = skyGreen * temp2 + temp1 * (1.0F - temp2);
            skyBlue = skyBlue * temp2 + temp1 * (1.0F - temp2);
        }
        
        temp1 = this.worldObj.getWeightedThunderStrength(partialTicks);
        
        if (temp1 > 0.0F) {
            temp2 = (skyRed * 0.3F + skyGreen * 0.59F + skyBlue * 0.11F) * 0.2F;
            float temp3 = 1.0F - temp1 * 0.75F;
            skyRed = skyRed * temp3 + temp2 * (1.0F - temp3);
            skyGreen = skyGreen * temp3 + temp2 * (1.0F - temp3);
            skyBlue = skyBlue * temp3 + temp2 * (1.0F - temp3);
        }
        
        if (this.worldObj.lastLightningBolt > 0) {
            temp2 = this.worldObj.lastLightningBolt - partialTicks;
            
            if (temp2 > 1.0F) {
                temp2 = 1.0F;
            }
            
            temp2 *= 0.45F;
            skyRed = skyRed * (1.0F - temp2) + 0.8F * temp2;
            skyGreen = skyGreen * (1.0F - temp2) + 0.8F * temp2;
            skyBlue = skyBlue * (1.0F - temp2) + 1.0F * temp2;
        }
        
        skyRed *= 2.0F;
        skyGreen *= 4.0F;
        skyBlue *= 0.5F;
        
        return Vec3.createVectorHelper(skyRed, skyGreen, skyBlue);
    }
    
    @Override
    public String getDimensionName() {
        return "The Corrupted Sector";
    }
    
    public static enum DayNightCycleMode {
        NORMAL, ETERNAL_DAY, ETERNAL_NIGHT
    }
    
}
