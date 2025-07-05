package net.sylviameows.tentorium.commands;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.database.LeaderboardResponse;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /leaderboard <mode>");
            return true;
        }
        String modeName = args[0];
        if (modeName == null || modeName.isEmpty()) {
            sender.sendMessage("Usage: /leaderboard <mode>");
            return true;
        }
        Mode mode = TentoriumCore.modes.get(modeName);

        if (mode instanceof TrackedScore tracked) {
            LeaderboardResponse response;
            if (sender instanceof Player player) {
                response = tracked.getLeaderboard(player);
            } else {
                response = tracked.getLeaderboard();
            }

            AtomicReference<Component> reference = new AtomicReference<>(Component.text("Leaderboard for "));
            reference.getAndUpdate(component -> component.append(mode.name()));

            response.forEach((place, lp) -> {
                if (lp == null) return;
                reference.getAndUpdate(component -> component.appendNewline());

                var line = Component.text(place + ". " + lp.name()).color(Palette.WHITE)
                        .append(Component.text(" - ").color(Palette.GRAY))
                        .append(Component.text(lp.score() + " " + tracked.leaderboardStatName()).color(Palette.WHITE));
                reference.getAndUpdate(component -> component.append(line));
            });

            sender.sendMessage(reference.get());
        } else {
            sender.sendMessage("Mode does not have trackable stats.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length <= 1) {
            List<String> suggestions = new ArrayList<>();
            TentoriumCore.modes.forEach((id, mode) -> {
                if (!(mode instanceof TrackedScore)) return;
                suggestions.add(id);
            });

            if (args.length == 1) {
                return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
            }

            return suggestions;
        } else {
            return Collections.emptyList();
        }
    }
}
