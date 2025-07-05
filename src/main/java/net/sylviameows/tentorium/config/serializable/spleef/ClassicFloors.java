package net.sylviameows.tentorium.config.serializable.spleef;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ClassicFloors extends FloorsConfig {
    public static String ALIAS = "classic_floors";
    private int layers;

    public ClassicFloors(int levels, int x1, int x2, int z1, int z2, int y, int gap) {
        super(x1, x2, z1, z2, y, gap);

        this.layers = levels;
    }

    public ClassicFloors(Map<String, Object> args) {
        super(args);

        this.layers = (int) args.get("layer_count");
    }

    public int layers() {
        return layers;
    }

    @Override
    protected Map<String, Object> addLayers(Map<String, Object> start) {
        start.put("layer_count", layers);
        return start;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        var data = super.serialize();
        data.put("==", ALIAS);
        return data;
    }
}
