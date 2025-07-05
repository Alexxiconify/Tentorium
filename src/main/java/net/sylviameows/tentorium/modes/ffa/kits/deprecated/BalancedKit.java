package net.sylviameows.tentorium.modes.ffa.kits.deprecated;

import net.sylviameows.tentorium.modes.ffa.kits.Kit;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;

public class BalancedKit extends Kit {
    public BalancedKit() {
        items.put(0, ItemUtilities.createItem(Material.STONE_SWORD));
        items.put(1, ItemUtilities.createItem(Material.BOW));
        items.put(8, ItemUtilities.createItem(Material.ARROW, 64));

        items.put(36, ItemUtilities.createItem(Material.NETHERITE_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.CHAINMAIL_LEGGINGS));
        items.put(38, ItemUtilities.createItem(Material.IRON_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.CHAINMAIL_HELMET));

        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
