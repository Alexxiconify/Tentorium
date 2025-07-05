package net.sylviameows.tentorium.modes.ffa.kits;

import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class KnightKit extends Kit {
    public KnightKit() {
        items.put(0, ItemUtilities.createItem(Material.IRON_SWORD, meta -> meta.addEnchant(Enchantment.SHARPNESS, 2, true)));
        items.put(1, ItemUtilities.createItem(Material.GOLDEN_APPLE, 1));
        items.put(2, ItemUtilities.createItem(Material.SPLASH_POTION, meta -> {
            if (meta instanceof PotionMeta potion) {
                potion.setBasePotionType(PotionType.SWIFTNESS);
            }
        }));

//        items.put(8, ItemUtilities.createItem(Material.COOKED_BEEF, 16));

        items.put(36, ItemUtilities.createItem(Material.IRON_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.IRON_LEGGINGS, meta -> meta.addEnchant(Enchantment.PROTECTION, 1, true)));
        items.put(38, ItemUtilities.createItem(Material.IRON_CHESTPLATE, meta -> meta.addEnchant(Enchantment.PROTECTION, 1, true)));
        items.put(39, ItemUtilities.createItem(Material.IRON_HELMET));

//        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
