package net.sylviameows.tentorium.config.serializable.spleef;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TNTFloors extends FloorsConfig {
    public static String ALIAS = "tnt_floors";
    private List<String> layers;

    public TNTFloors(List<String> levels, int x1, int x2, int z1, int z2, int y, int gap) {
        super(x1, x2, z1, z2, y, gap);

        this.layers = levels;
    }

    public TNTFloors(Map<String, Object> args) {
        super(args);

        this.layers = (List<String>) args.get("layers");
    }

    public List<String> layers() {
        return layers;
    }

    @Override
    protected Map<String, Object> addLayers(Map<String, Object> start) {
        start.put("layers", layers);
        return start;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var data = super.serialize();
        data.put("==", ALIAS);
        return data;
    }
}
