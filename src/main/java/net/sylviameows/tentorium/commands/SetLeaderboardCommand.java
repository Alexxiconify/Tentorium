package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.modes.TrackedScore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SetLeaderboardCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var location = player.getLocation();
            if (args.length < 1) {
                player.sendMessage("Usage: /setleaderboard <mode>");
                return true;
            }
            String modeName = args[0];
            if (modeName == null || modeName.isEmpty()) {
                player.sendMessage("Usage: /setleaderboard <mode>");
                return true;
            }
            Mode mode = TentoriumCore.modes.get(modeName);

            if (mode instanceof TrackedScore) {
                location.setPitch(0);
                location.setYaw(0);
                TentoriumCore.leaderboard().put(mode, location);
                player.sendMessage("Leaderboard location set for " + modeName);
            } else {
                sender.sendMessage("Mode does not have trackable stats.");
            }
        } else {
            sender.sendMessage("You must be a player to run this command.");
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
