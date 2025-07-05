package net.sylviameows.tentorium.modes.ffa.kits.deprecated;

import net.sylviameows.tentorium.modes.ffa.kits.Kit;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class MaceKit extends Kit {
    public MaceKit() {
        items.put(0, ItemUtilities.createItem(Material.MACE));

        var potion = ItemUtilities.createItem(Material.LINGERING_POTION, meta -> {
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setBasePotionType(PotionType.STRONG_LEAPING);
            }
        });

        items.put(1, potion);
        items.put(2, potion);
        items.put(3, potion);

        items.put(36, ItemUtilities.createItem(Material.LEATHER_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.LEATHER_LEGGINGS));
        items.put(38, ItemUtilities.createItem(Material.LEATHER_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.LEATHER_HELMET));

        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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
