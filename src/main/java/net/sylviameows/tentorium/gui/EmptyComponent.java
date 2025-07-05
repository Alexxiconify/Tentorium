package net.sylviameows.tentorium.gui;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.utilities.gui.AbstractButton;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EmptyComponent extends AbstractButton {
    private final Style style;

    public EmptyComponent(Style style) {
        this.style = style;
    }

    @Override
    public ItemStack getItem() {
        return style.getStack();
    }

    public enum Style {
        DARK(Material.BLACK_STAINED_GLASS_PANE),
        LIGHT(Material.GRAY_STAINED_GLASS_PANE),
        INVISIBLE(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        private ItemStack stack;

        Style(Material material) {
            var item = new ItemStack(material);
            var meta = item.getItemMeta();
            meta.displayName(Component.empty());
            meta.setHideTooltip(true);
            item.setItemMeta(meta);
            stack = item;
        }

        public ItemStack getStack() {
            return stack;
        }
    }
}
