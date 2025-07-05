package net.sylviameows.tentorium.modes;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.database.SQLite;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;

public abstract class Mode implements Listener {
    protected final ArrayList<Player> players = new ArrayList<>();
    protected SQLite database() {
        return TentoriumCore.database();
    }

    public Component name() {
        String name = StringUtils.capitalize(id());
        return Component.text(name);
    }

    public abstract String id();

    public void join(Player player) {
        players.add(player);
        var settings = PlayerManager.get(player);
        if (settings.mode() != null) settings.mode().leave(player);
        settings.mode(this);
    }

    public void leave(Player player) {
        players.remove(player);
    }

    @EventHandler
    public void dropItem(PlayerDropItemEvent event) {
        if (players.contains(event.getPlayer())) {
            event.setCancelled(true);
        }
    }


}
