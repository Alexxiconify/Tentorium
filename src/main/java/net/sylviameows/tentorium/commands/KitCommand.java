package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.gui.ffa.KitSelectionGUI;
import net.sylviameows.tentorium.modes.ffa.KitFFA;
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

public class KitCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var settings = PlayerManager.get(player);

            if (settings.mode() instanceof KitFFA) {
                if (args.length < 1) {
                    var gui = new KitSelectionGUI();
                    player.openInventory(gui.getInventory());
                    return true;
                }

                var kit = args[0].toLowerCase();
                if (KitFFA.kits().containsKey(kit) || kit.equals("shuffle")) {
                    settings.ffa().kit(kit);
                    player.sendMessage("Kit selected: " + kit);
                } else {
                    player.sendMessage("The kit you entered is not a valid kit.");
                }
            } else {
                player.sendMessage("The gamemode you are in does not support kits.");
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
            KitFFA.kits().forEach((id, _kit) -> {
                suggestions.add(id);
            });

            suggestions.add("shuffle");

            if (args.length == 1) {
                return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
            }

            return suggestions;
        } else {
            return Collections.emptyList();
        }
    }
}
