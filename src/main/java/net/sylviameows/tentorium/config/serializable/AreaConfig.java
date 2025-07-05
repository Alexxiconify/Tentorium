package net.sylviameows.tentorium.config.serializable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaConfig implements ConfigurationSerializable {
    public static String ALIAS = "area";

    protected World world;
    protected Location location;

    public AreaConfig(Location location) {
        this.world = location.getWorld();
        this.location = location;
    }

    public Location location() {
        return location;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("==", ALIAS);

        String world = this.world.getName();
        double[] coords = {
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        };

        data.put("world", world);
        data.put("location", coords);

        return data;
    }

    public AreaConfig(Map<String, Object> args) {
        List<?> list = (List<?>) args.get("location");
        List<Double> coords = list.stream().map((value) -> {
            if (value instanceof Double d) return d;
            if (value instanceof Number number) return number.doubleValue();
            return null;
        }).toList();

        world = Bukkit.getWorld((String) args.get("world"));
        location = new Location(
                world,
                coords.get(0),
                coords.get(1),
                coords.get(2),
                coords.get(3).floatValue(),
                coords.get(4).floatValue()
        );
    }
}
