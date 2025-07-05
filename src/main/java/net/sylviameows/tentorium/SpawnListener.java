package net.sylviameows.tentorium;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.commands.LeaveCommand;
import net.sylviameows.tentorium.gui.spawn.ModeSelectionGUI;
import net.sylviameows.tentorium.utilities.Palette;
import net.sylviameows.tentorium.values.Spawn;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

public class SpawnListener implements Listener {
    private boolean isPlayerUnoccupied(Entity entity) {
        return entity instanceof Player player && PlayerManager.get(player).mode() == null;
    }

    @EventHandler
    public void join(PlayerJoinEvent event) {
        event.joinMessage(event.getPlayer().name().color(Palette.YELLOW).append(Component.text(" joined the game!")));

        var player = event.getPlayer();
        player.teleportAsync(Spawn.LOCATION);

        var inventory = player.getInventory();
        inventory.clear();

        giveSelector(inventory);
    }


    @EventHandler
    public void join(PlayerQuitEvent event) {
        event.quitMessage(event.getPlayer().name().color(Palette.YELLOW).append(Component.text(" left the game!")));
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (isPlayerUnoccupied(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        if (isPlayerUnoccupied(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        if (isPlayerUnoccupied(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND) {
            if (!event.getItem().getPersistentDataContainer().getOrDefault(TentoriumCore.identififer("mode_selector"), PersistentDataType.BOOLEAN, false)) return;

            var gui = new ModeSelectionGUI();
            event.getPlayer().openInventory(gui.getInventory());
        }
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (isPlayerUnoccupied(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    public static void giveSelector(PlayerInventory inventory) {
        var compass = new ItemStack(Material.COMPASS);
        compass.editMeta(meta -> {
            meta.displayName(Component.text("Mode Selector").color(Palette.RED_LIGHT).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.getPersistentDataContainer().set(TentoriumCore.identififer("mode_selector"), PersistentDataType.BOOLEAN, true);
        });
        inventory.setItem(4, compass);
    }
}
