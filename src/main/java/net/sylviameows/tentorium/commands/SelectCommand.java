package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.modes.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var settings = PlayerManager.get(player);

            if (settings.mode() instanceof Parkour parkour) {
                if (args.length < 1) {
                    // todo: gui?
                    player.sendMessage("Usage: /select <map>");
                    return true;
                }

                var map = args[0].toLowerCase();
                if (Parkour.maps().containsKey(map)) {
                    settings.parkour().map(map);
                    var location = Parkour.maps().get(map);
                    settings.parkour().checkpoint(location);
                    player.teleportAsync(location);
                    player.sendMessage("Selected map: " + map);
                } else {
                    player.sendMessage("The map you entered is not a valid map.");
                }
            } else {
                player.sendMessage("The mode you are in does not support map selection.");
            }
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length <= 1) {
            List<String> suggestions = new ArrayList<>();
            Parkour.maps().forEach((id, _kit) -> {
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
