package tonius.thecorruptedsector.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import tonius.thecorruptedsector.config.TCSConfig.ConfigSection;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class ConfigGuiTCS extends GuiConfig {

    public ConfigGuiTCS(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(parentScreen), "thecorruptedsector", false, false, StatCollector.translateToLocal("thecorruptedsector.config.title"));
    }

    private static List<IConfigElement> getConfigElements(GuiScreen parent) {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        String prefix = "thecorruptedsector.config.";

        for (ConfigSection configSection : TCSConfig.configSections) {
            list.add(new ConfigElement<ConfigCategory>(TCSConfig.config.getCategory(configSection.toLowerCase()).setLanguageKey(prefix + configSection.id)));
        }

        return list;
    }

}
