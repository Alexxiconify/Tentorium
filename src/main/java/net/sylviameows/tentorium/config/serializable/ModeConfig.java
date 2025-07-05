package net.sylviameows.tentorium.config.serializable;

import net.sylviameows.tentorium.utilities.GameUtilities;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ModeConfig extends AreaConfig {
    public static String ALIAS = "mode";

    protected int void_level;
    protected GameUtilities.Area spawn_area;

    public ModeConfig(Location location, int void_level, GameUtilities.Area spawn_area) {
        super(location);

        this.void_level = void_level;
        this.spawn_area = spawn_area;
    }

    private ModeConfig(AreaConfig config, int void_level, GameUtilities.Area spawn_area) {
        this(config.location(), void_level, spawn_area);
    }

    public int voidLevel() {
        return void_level;
    }

    public void voidLevel(int y) {
        void_level = y;
    }

    public GameUtilities.Area region() {
        return spawn_area;
    }

    public void region(GameUtilities.Area area) {
        spawn_area = area;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var data = super.serialize();
        data.put("==", ALIAS);

        data.put("void_level", void_level);
        data.put("region", spawn_area);

        return data;
    }

    public static ModeConfig deserialize(Map<String, Object> args) {
        var config = new AreaConfig(args);

        return new ModeConfig(
                config,
                (int) args.get("void_level"),
                (GameUtilities.Area) args.get("region")
        );
    }
}
