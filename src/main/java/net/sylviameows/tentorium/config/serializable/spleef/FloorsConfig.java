package net.sylviameows.tentorium.config.serializable.spleef;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public abstract class FloorsConfig implements ConfigurationSerializable {
    protected int x1, x2;
    protected int z1, z2;
    protected int starting_y;

    protected int air_gap;

    protected FloorsConfig(int x1, int x2, int z1, int z2, int y, int gap) {
        this.x1 = x1;
        this.x2 = x2;
        this.z1 = z1;
        this.z2 = z2;
        this.starting_y = y;
        this.air_gap = gap;
    }

    protected FloorsConfig(Map<String, Object> args) {
        this.x1 = (int) args.get("x1");
        this.x2 = (int) args.get("x2");
        this.z1 = (int) args.get("z1");
        this.z2 = (int) args.get("z2");
        this.starting_y = (int) args.get("starting_y");
        this.air_gap = (int) args.get("air_gap");
    }

    public int x1() {
        return x1;
    }
    public int x2() {
        return x2;
    }
    public int z1() {
        return z1;
    }
    public int z2() {
        return z2;
    }
    public int y() {
        return starting_y;
    }
    public int gap() {
        return air_gap;
    }



    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("starting_y", starting_y);
        data.put("air_gap", air_gap);
        data.put("x1", x1);
        data.put("x2", x2);
        data.put("z1", z1);
        data.put("z2", z2);

        return addLayers(data);
    }

    abstract protected Map<String, Object> addLayers(Map<String, Object> start);
}
