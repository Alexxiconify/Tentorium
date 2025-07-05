package net.sylviameows.tentorium.modes;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.database.LeaderboardResponse;
import net.sylviameows.tentorium.utilities.GameUtilities;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.ArrayList;

public class BaseModes {
    // Mode class
    public static abstract class Mode implements Listener {
        protected final ArrayList<Player> players = new ArrayList<>();
        protected net.sylviameows.tentorium.database.SQLite database() {
            return TentoriumCore.database();
        }

        public Component name() {
            String id = id();
            String name = id.substring(0, 1).toUpperCase() + id.substring(1).toLowerCase();
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

    // TrackedScore interface
    public static interface TrackedScore {
        String leaderboardStatId();
        String leaderboardStatName();

        default LeaderboardResponse getLeaderboard() {
            return new LeaderboardResponse(leaderboardStatId());
        }

        default LeaderboardResponse getLeaderboard(Player player) {
            return new LeaderboardResponse(leaderboardStatId(), player);
        }
    }

    // ConfigurableMode interface
    public static interface ConfigurableMode {
        ModeConfig getOptions();
        default Location spawn() {
            return getOptions().location();
        }
        default GameUtilities.Area lobby() {
            return getOptions().region();
        }
        default int voidLevel() {
            return getOptions().voidLevel();
        }

        void reload();
    }

    // Bypass class
    public static class Bypass extends Mode {
        @Override
        public String id() {
            return "bypass";
        }

        @Override @EventHandler
        public void dropItem(PlayerDropItemEvent event) {
            // Does nothing, just prevents the spawn logic from affecting a player.
        }
    }
} 