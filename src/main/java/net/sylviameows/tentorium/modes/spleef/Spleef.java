package net.sylviameows.tentorium.modes.spleef;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.serializable.SpleefConfig;
import net.sylviameows.tentorium.modes.ConfigurableMode;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.utilities.Area;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;

public abstract class Spleef extends Mode implements TrackedScore, ConfigurableMode {
    protected boolean active = false;
    protected ArrayList<Player> alive = new ArrayList<>();

    protected SpleefConfig options;
    public void reload() {
        options = null;
    }


    @Override
    public void join(Player player) {
        player.teleportAsync(spawn()).whenComplete((result, ex) -> {
            super.join(player);
        });
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
    }

    @Override
    public void leave(Player player) {
        super.leave(player);
        checkThenReset(player);
    }

    private void checkThenReset(Player player) {
        alive.remove(player);

        if (alive.size() <= 1) {
            if (alive.isEmpty()) {
                reset();
                return;
            }
            var winner = alive.getFirst();
            winner(winner);

            players.forEach(p -> {
                p.sendMessage(winner.name().color(Palette.AQUA).append(Component.text(" won the match!").color(Palette.WHITE)));
                p.showTitle(Title.title(
                        Component.text(winner.getName()).color(Palette.AQUA),
                        Component.text("won the match!").color(Palette.WHITE),
                        Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
                ));

            });

            Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), this::reset, 20L * 3);
        }
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && players.contains(player)) event.setCancelled(true);
    }

    @EventHandler
    public void move(PlayerMoveEvent event) {
        var player = event.getPlayer();
        if (players.contains(player) && !alive.contains(player)) {
            var player_location = player.getLocation();
            if (lobby().contains(player_location)) return;

            var start_vector = player_location.toVector();
            var end_vector = spawn().toVector();

            var difference_vector = end_vector.subtract(start_vector);

            if (!active) {
                // join game

                if (!alive.isEmpty()) countdown(15);
                alive.add(player);

                setupPlayer(player);

                player.setVelocity(difference_vector.normalize().multiply(-1).add(new Vector(0, 0.3, 0)));

                return;
            }

            // launch backwards

            player.setVelocity(difference_vector.normalize());
        } else if (alive.contains(player) && event.getTo().y() <= voidLevel()) {

            player.teleportAsync(spawn());
            player.getInventory().clear();
            checkThenReset(player);
            // todo: death message
        }
    }

    protected void setupPlayer(Player player) {}
    protected void winner(Player player) {
        var name = PlainTextComponentSerializer.plainText().serialize(name());
        player.sendMessage(Component.text("(+1 "+ name.toLowerCase() +" win)").color(Palette.GRAY));
        player.playSound(Sound.sound(Key.key("minecraft", "entity.arrow.hit_player"), Sound.Source.PLAYER, 0.3f, 0.9f));
    }

    protected void countdown(int seconds_left) {
        if (seconds_left <= 0) {
            players.forEach(player -> {
                player.showTitle(Title.title(
                        Component.text("GO").color(Palette.LIME).decorate(TextDecoration.BOLD),
                        Component.empty(),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                ));
            });

            active = true;
            return;
        }

        players.forEach(player -> {
            TextColor color = Palette.YELLOW;
            Sound sound = Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, 1f, 2f);
            if (seconds_left <= 3) {
                player.showTitle(Title.title(
                        Component.text(seconds_left).color(color),
                        Component.empty(),
                        Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                ));
            } else {
                player.sendActionBar(Component.text(seconds_left).color(color));
            }
            if (seconds_left < 10) player.playSound(sound);

        });

        Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), () -> countdown(seconds_left -1), 20L);
    }


    protected void reset() {
        alive.forEach(player -> player.teleportAsync(spawn()));
        alive.clear();
        active = false;

        refresh();
    }

    /**
     * Reset the map.
     */
    abstract protected void refresh();

    @Override
    public String leaderboardStatName() {
        return "wins";
    }
}
