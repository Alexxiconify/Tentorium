package net.sylviameows.tentorium.modes;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class Parkour extends Mode {
    private final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("world"), -39.5, 48, -3.5, 90, 0);
    private static final HashMap<String, Location> maps = new HashMap<>();

    private final int VOID_LEVEL = 187;


    public Parkour() {
        maps.put("easy", new Location(Bukkit.getWorld("world"), -71.5, 205, 43.5, 180, 0));
        maps.put("normal", new Location(Bukkit.getWorld("world"), 27.5, 232, -25.5, 25, 0));
        maps.put("hard", new Location(Bukkit.getWorld("world"), 13.5, 217, -54.5, 90, 0));
    }

    @Override
    public String id() {
        return "parkour";
    }

    public static HashMap<String, Location> maps() {
        return maps;
    }

    @Override
    public void join(Player player) {
        super.join(player);

        PlayerManager.get(player).parkour().reset();
        player.teleportAsync(SPAWN_LOCATION);
        player.getInventory().clear();
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && players.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        if (players.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        if (players.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        var player = event.getPlayer();
        if (players.contains(player)) {
            if (event.getTo().y() <= VOID_LEVEL) {
                var settings = PlayerManager.get(player).parkour();
                if (settings.map() == null) return;
                var location = settings.checkpoint();
                if (location == null) location = SPAWN_LOCATION;

                location.setPitch(player.getPitch());
                location.setYaw(player.getYaw());
                player.teleportAsync(location);
            }
        }
    }

    @EventHandler
    public void checkpoint(PlayerInteractEvent event) {
        var player = event.getPlayer();
        if (!players.contains(player)) return;

        if (event.getAction() != Action.PHYSICAL) return;
        var interacted_block = event.getClickedBlock();
        if (interacted_block.getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
            event.setCancelled(true);

            var location = interacted_block.getLocation().add(0.5, 0, 0.5);
            var settings = PlayerManager.get(player).parkour();
            if (settings.checkpoint().equals(location)) return;

            settings.checkpoint(location);
            player.sendActionBar(Component.text("Obtained Checkpoint!"));
            player.playSound(Sound.sound(Key.key("block.note_block.chime"), Sound.Source.PLAYER, 1f, 1.9f));
        }
    }
}
