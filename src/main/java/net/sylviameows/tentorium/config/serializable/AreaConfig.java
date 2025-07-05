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
        Object locationObj = args.get("location");
        double[] coords;
        
        if (locationObj instanceof double[] coordsArray) {
            coords = coordsArray;
        } else if (locationObj instanceof List<?> list) {
            coords = list.stream()
                .mapToDouble(value -> {
                    if (value instanceof Double d) return d;
                    if (value instanceof Number number) return number.doubleValue();
                    return 0.0;
                })
                .toArray();
        } else {
            coords = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
        }

        world = Bukkit.getWorld((String) args.get("world"));
        location = new Location(
                world,
                coords[0],
                coords[1],
                coords[2],
                (float) coords[3],
                (float) coords[4]
        );
    }
}
