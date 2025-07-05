package net.sylviameows.tentorium.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Area implements ConfigurationSerializable {
    public static String ALIAS = "region";

    private Location a;
    private Location b;

    public Area(Location a, Location b) {
        this.a = a;
        this.b = b;
    }

    public boolean contains(Location location) {
        var min_x = Math.min(a.x(), b.x());
        var max_x = Math.max(a.x(), b.x());

        if (location.x() > max_x || location.x() < min_x) return false;

        var min_y = Math.min(a.y(), b.y());
        var max_y = Math.max(a.y(), b.y());

        if (location.y() > max_y || location.y() < min_y) return false;

        var min_z = Math.min(a.z(), b.z());
        var max_z = Math.max(a.z(), b.z());

        return !(location.z() > max_z) && !(location.z() < min_z);
    }

    public void forEach(Consumer<Block> consumer) {
        var min_x = Math.min(a.getBlockX(), b.getBlockX());
        var max_x = Math.max(a.getBlockX(), b.getBlockX());
        var min_y = Math.min(a.getBlockY(), b.getBlockY());
        var max_y = Math.max(a.getBlockY(), b.getBlockY());
        var min_z = Math.min(a.getBlockZ(), b.getBlockZ());
        var max_z = Math.max(a.getBlockZ(), b.getBlockZ());

        var world = a.getWorld();
        for (int x = min_x; x < max_x; x++) {
            for (int y = min_y; x < max_y; y++) {
                for (int z = min_z; x < max_z; x++) {
                    var block = world.getBlockAt(x, y, z);
                    consumer.accept(block);
                }
            }
        }
    }

    public void shift(Vector vector) {
        a.add(vector);
        b.add(vector);
    }


    public Location a() {
        return a;
    }
    public void a(Location location) {
        this.a = location;
    }

    public Location b() {
        return b;
    }
    public void b(Location location) {
        this.b = location;
    }

    public World world() { return a.getWorld(); }
    public void world(World world) {
        a.setWorld(world);
        b.setWorld(world);
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("a", new int[]{a.getBlockX(), a.getBlockY(), a.getBlockZ()});
        data.put("b", new int[]{b.getBlockX(), b.getBlockY(), b.getBlockZ()});

        data.put("==", ALIAS);

        return data;
    }

    public static Area deserialize(Map<String, Object> args) {
        List<Integer> a = (List<Integer>) args.get("a");
        List<Integer> b = (List<Integer>) args.get("b");

        var location_a = new Location(Bukkit.getWorld("world"), a.get(0), a.get(1), a.get(2));
        var location_b = new Location(Bukkit.getWorld("world"), b.get(0), b.get(1), b.get(2));

        return new Area(location_a, location_b);
    }
}
