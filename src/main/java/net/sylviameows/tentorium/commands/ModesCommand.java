package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.gui.spawn.ModeSelectionGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ModesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var gui = new ModeSelectionGUI();
            player.openInventory(gui.getInventory());
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }
}
