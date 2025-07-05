package net.sylviameows.tentorium;

import net.sylviameows.tentorium.utilities.gui.AbstractGUI;
import net.sylviameows.tentorium.utilities.gui.GUIHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InventoryListener implements Listener {
    private final Set<UUID> delays = new HashSet<>();
    private final Plugin plugin;

    public InventoryListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void openListener(InventoryOpenEvent event){
        Inventory inv = event.getInventory();
        AbstractGUI gui = GUIHandler.getRegisteredGUI(inv);

        if (gui == null) {
            return;
        }

        gui.onOpen(event);
    }

    @EventHandler
    public void closeListener(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        AbstractGUI gui = GUIHandler.getRegisteredGUI(inv);

        if (gui == null) {
            return;
        }

        gui.onClose(event);
        GUIHandler.unregisterGUI(inv);
    }

    @EventHandler
    public void clickListener(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        AbstractGUI gui = GUIHandler.getRegisteredGUI(inv);
        if (gui == null){
            return;
        }

        event.setCancelled(true);

        if (event.getClickedInventory() != inv) {
            return;
        }

        if (hasDelay(event.getWhoClicked().getUniqueId())) {
            return;
        }

        gui.onClick(event);
    }

    private boolean hasDelay(UUID uuid) {
        if (delays.contains(uuid)){
            return true;
        }
        delays.add(uuid);
        Bukkit.getScheduler().runTaskLater(plugin, () -> delays.remove(uuid), 10);
        return false;
    }

}
