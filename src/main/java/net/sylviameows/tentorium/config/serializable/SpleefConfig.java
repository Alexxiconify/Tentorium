package net.sylviameows.tentorium.config.serializable;

import net.sylviameows.tentorium.config.serializable.spleef.FloorsConfig;
import net.sylviameows.tentorium.utilities.Area;
import org.bukkit.Location;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class SpleefConfig extends ModeConfig {
    public static String ALIAS = "spleef_mode";
    FloorsConfig floors;

    public SpleefConfig(FloorsConfig floors, Location location, int void_level, Area spawn_area) {
        super(location, void_level, spawn_area);

        this.floors = floors;
    }

    public SpleefConfig(ModeConfig config, FloorsConfig floors) {
        super(config.location, config.void_level, config.spawn_area);

        this.floors = floors;
    }

    public FloorsConfig floors() {
        return floors;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var data = super.serialize();
        data.put("==", ALIAS);

        data.put("floors", floors);

        return data;
    }

    public static SpleefConfig deserialize(Map<String, Object> args) {
        var config = ModeConfig.deserialize(args);

        return new SpleefConfig(
                config,
                (FloorsConfig) args.get("floors")
        );
    }
}
