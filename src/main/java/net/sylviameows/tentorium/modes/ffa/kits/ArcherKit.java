package net.sylviameows.tentorium.modes.ffa.kits;

import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class ArcherKit extends Kit {
    public ArcherKit() {
        items.put(0, ItemUtilities.createItem(Material.STONE_SWORD));
        items.put(1, ItemUtilities.createItem(Material.BOW, meta -> {
            meta.addEnchant(Enchantment.PUNCH, 2, true);
            meta.addEnchant(Enchantment.POWER, 1, true);
            meta.addEnchant(Enchantment.INFINITY, 1, true);
        }));
        items.put(2, ItemUtilities.createItem(Material.SPLASH_POTION, meta -> {
            if (meta instanceof PotionMeta potion) {
                potion.setBasePotionType(PotionType.SWIFTNESS);
            }
        }));

//        items.put(8, ItemUtilities.createItem(Material.COOKED_BEEF, 16));
        items.put(8, ItemUtilities.createItem(Material.ARROW));

        items.put(36, ItemUtilities.createItem(Material.LEATHER_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.IRON_LEGGINGS));
        items.put(38, ItemUtilities.createItem(Material.IRON_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.LEATHER_HELMET));

//        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
