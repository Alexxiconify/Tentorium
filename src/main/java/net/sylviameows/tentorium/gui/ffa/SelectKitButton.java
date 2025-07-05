package net.sylviameows.tentorium.gui.ffa;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.gui.IconComponent;
import net.sylviameows.tentorium.modes.ffa.KitFFA;
import net.sylviameows.tentorium.utilities.gui.ClickableButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.List;

public class SelectKitButton extends IconComponent implements ClickableButton {
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
