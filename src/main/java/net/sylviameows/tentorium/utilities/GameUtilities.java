package net.sylviameows.tentorium.utilities;

import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GameUtilities {
    // Area class
    public static class Area implements ConfigurationSerializable {
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
                for (int y = min_y; y < max_y; y++) {
                    for (int z = min_z; z < max_z; z++) {
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
            Object aObj = args.get("a");
            Object bObj = args.get("b");
            
            int[] aCoords;
            int[] bCoords;
            
            if (aObj instanceof int[] aArray) {
                aCoords = aArray;
            } else if (aObj instanceof List<?> aList) {
                aCoords = aList.stream()
                    .mapToInt(obj -> obj instanceof Number num ? num.intValue() : 0)
                    .toArray();
            } else {
                aCoords = new int[]{0, 0, 0};
            }
            
            if (bObj instanceof int[] bArray) {
                bCoords = bArray;
            } else if (bObj instanceof List<?> bList) {
                bCoords = bList.stream()
                    .mapToInt(obj -> obj instanceof Number num ? num.intValue() : 0)
                    .toArray();
            } else {
                bCoords = new int[]{0, 0, 0};
            }

            var location_a = new Location(Bukkit.getWorld("world"), aCoords[0], aCoords[1], aCoords[2]);
            var location_b = new Location(Bukkit.getWorld("world"), bCoords[0], bCoords[1], bCoords[2]);

            return new Area(location_a, location_b);
        }
    }

    // Palette class
    public static class Palette {
        public static TextColor WHITE = TextColor.color(0xFFFFFF);
        public static TextColor MEDAL_SILVER = TextColor.color(0xD7D7D7);
        public static TextColor GRAY = TextColor.color(0xAAAAAA);
        public static TextColor DARK_GRAY = TextColor.color(0x555555);
        public static TextColor BLACK = TextColor.color(0x000000);

        public static TextColor RED_DARK = TextColor.color(0xAA0000);
        public static TextColor RED = TextColor.color(0xFF0000);
        public static TextColor RED_LIGHT = TextColor.color(0xFF5555);

        public static TextColor MEDAL_BRONZE = TextColor.color(0xA77044);

        public static TextColor GOLD = TextColor.color(0xFFAA00);

        public static TextColor MEDAL_GOLD = TextColor.color(0xFEE101);

        public static TextColor YELLOW = TextColor.color(0xFFFF00);

        public static TextColor LIME = TextColor.color(0xAAFF55);

        public static TextColor GREEN_DARK = TextColor.color(0x00AA00);
        public static TextColor GREEN = TextColor.color(0x55FF55);

        public static TextColor MINT = TextColor.color(0x87ffdf);

        public static TextColor AQUA_DARK = TextColor.color(0x00AAAA);
        public static TextColor AQUA = TextColor.color(0x55FFFF);

        public static TextColor BLUE_DARK = TextColor.color(0x0000AA);
        public static TextColor BLUE = TextColor.color(0x5555FF);

        public static TextColor PURPLE = TextColor.color(0xAA00AA);
        public static TextColor PURPLE_LIGHT = TextColor.color(0xFF55FF);
    }

    // ItemUtilities class
    public static class ItemUtilities {
        public static ItemStack createItem(Material material) {
            return createItem(material, 1);
        }
        public static ItemStack createItem(Material material, int count) {
            var item = new ItemStack(material, count);
            item.editMeta(meta -> meta.setUnbreakable(true));
            return item;
        }
        public static ItemStack createItem(Material material, int count, Consumer<? super ItemMeta> edit) {
            var item = createItem(material, count);
            item.editMeta(edit);
            return item;
        }
        public static ItemStack createItem(Material material, Consumer<? super ItemMeta> edit) {
            return createItem(material, 1, edit);
        }
    }
} 