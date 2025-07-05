package net.sylviameows.tentorium.commands;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StatsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            var db = TentoriumCore.database();
            var uuid = player.getUniqueId().toString();

            player.sendMessage(Component.text("Game Stats:").color(Palette.LIME));
            TentoriumCore.modes.forEach((id, mode) -> {
                if (mode instanceof TrackedScore ts) {
                    player.sendMessage(Component.text(": ").color(Palette.GRAY).append(mode.name())
                            .append(Component.text(" - ").color(Palette.GRAY))
                            .append(Component.text(db.fetchInt(uuid, ts.leaderboardStatId()) + " " + ts.leaderboardStatName()).color(Palette.WHITE))
                    );
                }
            });
        } else {
            sender.sendMessage("You must be a player to run this command.");
        }
        return true;
    }
}
