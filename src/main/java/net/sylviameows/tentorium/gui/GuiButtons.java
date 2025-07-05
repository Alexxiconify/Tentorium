package net.sylviameows.tentorium.gui;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.modes.FFAModes.KitFFA;
import net.sylviameows.tentorium.utilities.gui.ClickableButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;

public abstract class GuiButtons {
    // SelectKitButton logic
    public static class SelectKitButton extends GuiComponents.IconComponent implements ClickableButton {
        private final String kit;

        public SelectKitButton(Component name, Material material, String kit) {
            this(name, material, null, kit);
        }

        public SelectKitButton(Component name, Material material, List<Component> lore, String kit) {
            super(name, material, lore);
            this.kit = kit;
        }

        @Override
        public void onClick(InventoryClickEvent click) {
            if (click.getWhoClicked() instanceof Player player) {
                var settings = PlayerManager.get(player);
                settings.ffa().kit(kit);
                if (settings.mode() instanceof KitFFA mode && mode.lobby().contains(player.getLocation())) mode.giveKit(player);
                var kit_name = click.getCurrentItem().getItemMeta().displayName();
                if (kit_name == null) kit_name = Component.empty();
                player.sendMessage(Component.text("Selected Kit: ").append(kit_name));
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
        }
    }

    // SelectModeButton logic
    public static class SelectModeButton extends GuiComponents.IconComponent implements ClickableButton {
        private final String mode;

        public SelectModeButton(Component name, Material material, String mode) {
            this(name, material, null, mode);
        }

        public SelectModeButton(Component name, Material material, List<Component> lore, String mode) {
            super(name, material, lore);
            this.mode = mode;
        }

        @Override
        public void onClick(InventoryClickEvent click) {
            if (click.getWhoClicked() instanceof Player player) {
                var mode = TentoriumCore.modes.get(this.mode);
                if (mode == null) return;
                mode.join(player);
                var kit_name = click.getCurrentItem().getItemMeta().displayName();
                if (kit_name == null) kit_name = Component.empty();
                player.sendMessage(Component.text("Joining Mode: ").append(kit_name));
                player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            }
        }
    }
} 