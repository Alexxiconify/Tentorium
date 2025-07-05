package net.sylviameows.tentorium.modes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.utilities.GameUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class DeprecatedKits {
    // AxeKit class
    public static class AxeKit extends GameKits.Kit {
        public AxeKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.STONE_AXE));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.CROSSBOW, meta -> {
                meta.addEnchant(Enchantment.PIERCING, 1, true);
            }));
            items.put(8, GameUtilities.ItemUtilities.createItem(Material.ARROW, 64));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.NETHERITE_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.LEATHER_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.NETHERITE_HELMET));
        }

        @Override
        public String id() {
            return "axe";
        }

        @Override
        public Material icon() {
            return Material.STONE_AXE;
        }
    }

    // BalancedKit class
    public static class BalancedKit extends GameKits.Kit {
        public BalancedKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.STONE_SWORD));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.BOW));
            items.put(8, GameUtilities.ItemUtilities.createItem(Material.ARROW, 64));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.NETHERITE_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.IRON_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_HELMET));

            items.put(40, GameUtilities.ItemUtilities.createItem(Material.SHIELD));
        }

        @Override
        public String id() {
            return "balanced";
        }

        @Override
        public Material icon() {
            return Material.STONE_SWORD;
        }
    }

    // MaceKit class
    public static class MaceKit extends GameKits.Kit {
        public MaceKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.STONE_AXE));

            items.put(1, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE));
            items.put(2, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE));
            items.put(3, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.LEATHER_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.LEATHER_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.LEATHER_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.LEATHER_HELMET));

            items.put(40, GameUtilities.ItemUtilities.createItem(Material.SHIELD));
        }

        @Override
        public String id() {
            return "mace";
        }

        @Override
        public Material icon() {
            return Material.STONE_AXE;
        }
    }

    // ParasiteKit class
    public static class ParasiteKit extends GameKits.Kit {
        public ParasiteKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_SWORD));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.BOW));
            items.put(8, GameUtilities.ItemUtilities.createItem(Material.ARROW, 64));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.DIAMOND_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.DIAMOND_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.LEATHER_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.LEATHER_HELMET));

            items.put(40, GameUtilities.ItemUtilities.createItem(Material.SHIELD));
        }

        @Override
        public String id() {
            return "parasite";
        }

        @Override
        public Material icon() {
            return Material.SILVERFISH_SPAWN_EGG;
        }
    }

    // SpiderKit class
    public static class SpiderKit extends GameKits.Kit {
        public SpiderKit() {
            items.put(0, GameUtilities.ItemUtilities.createItem(Material.DIAMOND_SWORD));
            items.put(1, GameUtilities.ItemUtilities.createItem(Material.COBWEB, 12, meta -> {
                meta.lore(List.of(Component.text("Will disappear after 12 seconds").color(NamedTextColor.GRAY).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
            }));
            items.put(8, GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE));

            items.put(36, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_BOOTS));
            items.put(37, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_LEGGINGS));
            items.put(38, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_CHESTPLATE));
            items.put(39, GameUtilities.ItemUtilities.createItem(Material.CHAINMAIL_HELMET));

            items.put(40, GameUtilities.ItemUtilities.createItem(Material.SHIELD));
        }

        @Override
        public String id() {
            return "spider";
        }

        @Override
        public Material icon() {
            return Material.COBWEB;
        }
    }
} 