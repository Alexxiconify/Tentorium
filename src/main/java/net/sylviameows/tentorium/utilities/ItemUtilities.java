package net.sylviameows.tentorium.utilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public class ItemUtilities {
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
