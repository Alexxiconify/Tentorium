package net.sylviameows.tentorium.modes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

/**
 * Does nothing, just prevents the spawn logic from affecting a player.
 */
public class Bypass extends Mode {
    @Override
    public String id() {
        return "bypass";
    }

    @Override @EventHandler
    public void dropItem(PlayerDropItemEvent event) {

    }
}
