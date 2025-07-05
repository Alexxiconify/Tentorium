package net.sylviameows.tentorium.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.utilities.gui.AbstractButton;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class IconComponent extends AbstractButton {
    private final Component name;
    private final Material material;
    private final List<Component> lore;

    public IconComponent(Component name, Material material) {
        this(name, material, null);
    }

    public IconComponent(Component name, Material material, List<Component> lore) {
        this.name = name;
        this.material = material;
        this.lore = lore;
    }

    @Override
    public ItemStack getItem() {
        var item = new ItemStack(material);
        var meta = item.getItemMeta();
        meta.displayName(name.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        if (lore != null) {
            var new_lore = lore.stream().map(component -> component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)).toList();
            meta.lore(new_lore);
        }
        item.setItemMeta(meta);
        item.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, ItemFlag.HIDE_ATTRIBUTES);
        return item;
    }
}
