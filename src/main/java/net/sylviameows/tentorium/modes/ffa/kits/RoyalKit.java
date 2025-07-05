package net.sylviameows.tentorium.modes.ffa.kits;

import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class RoyalKit extends Kit {
    public RoyalKit() {
        items.put(0, ItemUtilities.createItem(Material.DIAMOND_SWORD, meta -> meta.addEnchant(Enchantment.SHARPNESS, 1, true)));
        items.put(1, ItemUtilities.createItem(Material.GOLDEN_APPLE, 2));

//        items.put(8, ItemUtilities.createItem(Material.COOKED_BEEF, 16));

        items.put(36, ItemUtilities.createItem(Material.IRON_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.IRON_LEGGINGS, meta -> meta.addEnchant(Enchantment.PROTECTION, 1, true)));
        items.put(38, ItemUtilities.createItem(Material.DIAMOND_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.IRON_HELMET));

//        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
