package net.sylviameows.tentorium.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class GuiComponents {
    // IconComponent logic
    public static class IconComponent extends net.sylviameows.tentorium.utilities.gui.AbstractButton {
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
            item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            return item;
        }
    }

    // EmptyComponent logic
    public static class EmptyComponent extends net.sylviameows.tentorium.utilities.gui.AbstractButton {
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
            private final ItemStack stack;

            Style(Material material) {
                var item = new ItemStack(material);
                var meta = item.getItemMeta();
                meta.displayName(Component.empty());
                item.setItemMeta(meta);
                stack = item;
            }

            public ItemStack getStack() {
                return stack;
            }
        }
    }
} 