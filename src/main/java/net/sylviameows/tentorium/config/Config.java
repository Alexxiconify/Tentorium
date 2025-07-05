package net.sylviameows.tentorium.config;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.serializable.AreaConfig;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.config.serializable.SpleefConfig;
import net.sylviameows.tentorium.config.serializable.spleef.ClassicFloors;
import net.sylviameows.tentorium.config.serializable.spleef.TNTFloors;
import net.sylviameows.tentorium.utilities.Area;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public class Config {
    public final TentoriumCore core;
    private static FileConfiguration config;

    public Config(TentoriumCore core) {
        this.core = core;

        ConfigurationSerialization.registerClass(Area.class, Area.ALIAS);

        ConfigurationSerialization.registerClass(AreaConfig.class, AreaConfig.ALIAS);
        ConfigurationSerialization.registerClass(ModeConfig.class, ModeConfig.ALIAS);
        ConfigurationSerialization.registerClass(SpleefConfig.class, SpleefConfig.ALIAS);

        ConfigurationSerialization.registerClass(ClassicFloors.class, ClassicFloors.ALIAS);
        ConfigurationSerialization.registerClass(TNTFloors.class, TNTFloors.ALIAS);

        core.saveResource("config.yml", false);
        Config.config = core.getConfig();
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void save() {
        TentoriumCore.instance().saveConfig();
    }


}
