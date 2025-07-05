package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.values.Spawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static net.sylviameows.tentorium.SpawnListener.giveSelector;

public class LeaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var settings = PlayerManager.get(player);

            if (settings.mode() != null) {
                settings.mode().leave(player);
                settings.mode(null);
            }

            var inventory = player.getInventory();
            inventory.clear();
            player.clearActivePotionEffects();
            player.teleportAsync(Spawn.LOCATION);

            giveSelector(inventory);
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }
}
