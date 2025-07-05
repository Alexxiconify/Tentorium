package net.sylviameows.tentorium.commands;

import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ModeAlias implements CommandExecutor {
    private final String mode;

    public ModeAlias(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var mode = TentoriumCore.modes.get(this.mode);
            if (mode != null) {
                mode.join(player);
            } else {
                player.sendMessage("Unknown mode: " + this.mode);
            }
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }
}
