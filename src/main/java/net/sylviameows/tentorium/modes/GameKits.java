package net.sylviameows.tentorium.modes;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.utilities.GameUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.HashMap;

public class GameKits {
    // Base Kit class
    public static abstract class Kit {
        protected HashMap<Integer, ItemStack> items = new HashMap<>();

        abstract public String id();
        abstract public Material icon();

        public Component name() {
            return Component.text(id());
        }
        public Component description() {
            return Component.empty();
        }

        public void apply(Player player) {
            var inventory = player.getInventory();
            inventory.clear();

            items.forEach(inventory::setItem);
        }
    }

    // KnightKit class
    public static class KnightKit extends Kit {
        public KnightKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.IRON_SWORD, meta -> meta.addEnchant(Enchantment.SHARPNESS, 2, true)));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE, 1));
            items.put(2, GameUtilities.ItemUtilities.createItem(Material.SPLASH_POTION, meta -> {
                if (meta instanceof PotionMeta potion) {
                    potion.setBasePotionType(PotionType.SWIFTNESS);
                }
            }));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.IRON_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.IRON_LEGGINGS, meta -> meta.addEnchant(Enchantment.PROTECTION, 1, true)));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.IRON_CHESTPLATE, meta -> meta.addEnchant(Enchantment.PROTECTION, 1, true)));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.IRON_HELMET));
        }

        @Override
        public String id() {
            return "knight";
        }

        @Override
        public Material icon() {
            return Material.IRON_SWORD;
        }
    }

    // RoyalKit class
    public static class RoyalKit extends Kit {
        public RoyalKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.DIAMOND_SWORD, meta -> meta.addEnchant(Enchantment.SHARPNESS, 1, true)));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE, 2));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.IRON_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.IRON_LEGGINGS, meta -> meta.addEnchant(Enchantment.PROJECTILE_PROTECTION, 1, true)));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.DIAMOND_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.IRON_HELMET));
        }

        @Override
        public String id() {
            return "royal";
        }

        @Override
        public Material icon() {
            return Material.GOLDEN_HELMET;
        }
    }

    // ArcherKit class
    public static class ArcherKit extends Kit {
        public ArcherKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.STONE_SWORD));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.BOW, meta -> {
                meta.addEnchant(Enchantment.PUNCH, 2, true);
                meta.addEnchant(Enchantment.POWER, 1, true);
                meta.addEnchant(Enchantment.INFINITY, 1, true);
            }));
            items.put(2, GameUtilities.ItemUtilities.createItem(Material.SPLASH_POTION, meta -> {
                if (meta instanceof PotionMeta potion) {
                    potion.setBasePotionType(PotionType.SWIFTNESS);
                }
            }));

            items.put(8, GameUtilities.ItemUtilities.createItem(Material.ARROW));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.LEATHER_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.IRON_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.IRON_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.LEATHER_HELMET));
        }

        @Override
        public String id() {
            return "archer";
        }

        @Override
        public Material icon() {
            return Material.BOW;
        }
    }
} 