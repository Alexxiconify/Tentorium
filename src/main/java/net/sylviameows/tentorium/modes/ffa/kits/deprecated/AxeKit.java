package net.sylviameows.tentorium.modes.ffa.kits.deprecated;

import net.sylviameows.tentorium.modes.ffa.kits.Kit;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

public class AxeKit extends Kit {
    public AxeKit() {
        items.put(0, ItemUtilities.createItem(Material.STONE_AXE));
        items.put(1, ItemUtilities.createItem(Material.CROSSBOW, meta -> {
            meta.addEnchant(Enchantment.PIERCING, 1, true);
        }));
        items.put(8, ItemUtilities.createItem(Material.ARROW, 64));

        items.put(36, ItemUtilities.createItem(Material.NETHERITE_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.LEATHER_LEGGINGS));
        items.put(38, ItemUtilities.createItem(Material.CHAINMAIL_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.NETHERITE_HELMET));

//        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
