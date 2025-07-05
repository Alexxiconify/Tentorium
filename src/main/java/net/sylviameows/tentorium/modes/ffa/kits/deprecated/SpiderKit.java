package net.sylviameows.tentorium.modes.ffa.kits.deprecated;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.modes.ffa.kits.Kit;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import org.bukkit.Material;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

public class SpiderKit extends Kit {
    public SpiderKit() {
        items.put(0, ItemUtilities.createItem(Material.DIAMOND_SWORD));
        items.put(1, ItemUtilities.createItem(Material.COBWEB, 12, meta -> {
            meta.lore(List.of(Component.text("Will disappear after 12 seconds").color(NamedTextColor.GRAY).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        }));
        items.put(8, ItemUtilities.createItem(Material.SPLASH_POTION, meta -> {
            if (meta instanceof PotionMeta) {
                ((PotionMeta) meta).setBasePotionType(PotionType.WEAVING);
            }
        }));

        items.put(36, ItemUtilities.createItem(Material.CHAINMAIL_BOOTS));
        items.put(37, ItemUtilities.createItem(Material.CHAINMAIL_LEGGINGS));
        items.put(38, ItemUtilities.createItem(Material.CHAINMAIL_CHESTPLATE));
        items.put(39, ItemUtilities.createItem(Material.CHAINMAIL_HELMET));

        items.put(40, ItemUtilities.createItem(Material.SHIELD));
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