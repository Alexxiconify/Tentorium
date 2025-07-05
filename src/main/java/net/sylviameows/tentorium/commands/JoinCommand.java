package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class JoinCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("Usage: /join <mode>");
            return true;
        }
        
        if (sender instanceof Player player) {
            String modeName = args[0];
            if (modeName == null || modeName.isEmpty()) {
                player.sendMessage("Unknown mode: " + modeName);
                return true;
            }
            var mode = TentoriumCore.modes.get(modeName);
            if (mode != null) {
                mode.join(player);
            } else {
                player.sendMessage("Unknown mode: " + modeName);
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
            TentoriumCore.modes.forEach((id, _mode) -> {
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
