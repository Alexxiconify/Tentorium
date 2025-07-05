package net.sylviameows.tentorium.modes;

import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.utilities.Area;
import org.bukkit.Location;

public interface ConfigurableMode {
    ModeConfig getOptions();
    default Location spawn() {
        return getOptions().location();
    }
    default Area lobby() {
        return getOptions().region();
    }
    default int voidLevel() {
        return getOptions().voidLevel();
    }

    void reload();
}
