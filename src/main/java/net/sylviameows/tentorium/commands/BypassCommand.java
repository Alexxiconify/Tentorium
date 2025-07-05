package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.modes.Bypass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class BypassCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var settings = PlayerManager.get(player);
            if (settings.mode() instanceof Bypass mode) {
                mode.leave(player);
                settings.mode(null);
                player.sendMessage("Disabled protection bypass.");
            } else {
                if (settings.mode() != null) settings.mode().leave(player);
                settings.mode(new Bypass());
                player.sendMessage("Enabled protection bypass.");
            }
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        return Collections.emptyList();
    }
}
