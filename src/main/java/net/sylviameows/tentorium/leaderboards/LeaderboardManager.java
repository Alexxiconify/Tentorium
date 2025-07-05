package net.sylviameows.tentorium.leaderboards;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TextDisplay;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardManager {
    private final HashMap<Mode, Leaderboard> boards = new HashMap();
    private LeaderboardTask task;

    public class Leaderboard {
        private Location location;
        private @Nullable TextDisplay board;

        public Leaderboard(Location location, @Nullable TextDisplay board) {
            this.location = location;
            this.board = board;
        }

        public Location location() {
            return location;
        }
        public void location(Location location) {
            this.location = location;
        }

        public TextDisplay board() {
            return board;
        }
        public void board(TextDisplay board) {
            this.board = board;
        }
    }


    private String NUMBER = "♯";

    public LeaderboardManager(TentoriumCore core) {
        task = new LeaderboardTask(core);

        Bukkit.getWorlds().forEach(world -> {
            world.getEntitiesByClass(TextDisplay.class).forEach(entity -> {
                if (entity.getPersistentDataContainer().has(TentoriumCore.identififer("leaderboard"))) {
                    entity.remove();
                }
            });
        });

        TentoriumCore.modes.forEach((string, mode) -> {
            if (!(mode instanceof TrackedScore)) return;

            var location = Config.get().getLocation("leaderboard."+mode.id());
            if (location == null) return;

            boards.put(mode, new Leaderboard(location, createDisplay(mode, location)));
        });
    }

    public void updateAll() {
        boards.forEach(((mode, leaderboard) -> {
            leaderboard.board().text(text(mode));
        }));
    }

    public void update(Mode mode) {
        var board = boards.get(mode);
        if (board == null) return;;

        if (board.board() != null) {
            board.board().text(text(mode));
        }
    }

    public void put(Mode mode, Location location) {
        var board = new Leaderboard(location, createDisplay(mode, location));
        var old = boards.get(mode);
        if (old != null) {
            old.board.remove();
        }
        boards.put(mode, board);
        Config.get().set("leaderboard."+mode.id(), location);
        Config.save();
    }

    protected TextDisplay createDisplay(Mode mode, Location location) {
        var display = (TextDisplay) location.getWorld().spawnEntity(location.toCenterLocation(), EntityType.TEXT_DISPLAY);
        display.setAlignment(TextDisplay.TextAlignment.CENTER);
        display.setBackgroundColor(Color.fromARGB(0x00000000));
        display.setShadowed(true);
        display.setBillboard(Display.Billboard.VERTICAL);

        display.text(text(mode));

        display.getPersistentDataContainer().set(TentoriumCore.identififer("leaderboard"), PersistentDataType.BOOLEAN, true);

        return display;
    }

    protected Component text(Mode mode) {
        if (mode instanceof TrackedScore ts) {
            var stat_name = ts.leaderboardStatName();
            var title = mode.name().decorate(TextDecoration.BOLD);

            var response = ts.getLeaderboard();

            AtomicReference<Component> reference = new AtomicReference<>(Component.empty().append(title).appendNewline());
            response.forEach((place, player) -> {
                var component = reference.get();

                component = component.appendNewline();
                TextColor color;
                switch (place) {
                    case 1 -> color = Palette.MEDAL_GOLD;
                    case 2 -> color = Palette.MEDAL_SILVER;
                    case 3 -> color = Palette.MEDAL_BRONZE;
                    default -> color = Palette.DARK_GRAY;
                }

                var placement = Component.text(NUMBER+place+" ").color(color);

                Component name = Component.text("---").color(Palette.WHITE).decorate(TextDecoration.STRIKETHROUGH);
                Component score = Component.text("---").color(Palette.GRAY).decorate(TextDecoration.STRIKETHROUGH);
                if (player != null) {
                    name = Component.text(player.name()).color(Palette.WHITE);
                    score = Component.text(player.score()+" "+stat_name).color(Palette.GRAY);
                }

                component = component.append(placement).append(name);
                component = component.appendNewline();

                component = component.append(score).appendNewline();

                reference.set(component);
            });

            return reference.get();
        } else {
            return Component.empty();
        }
    }
}
