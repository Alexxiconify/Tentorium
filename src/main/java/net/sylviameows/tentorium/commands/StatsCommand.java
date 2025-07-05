package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.modes.Bypass;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class StatsCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player) {
            var db = TentoriumCore.database();
            var uuid = player.getUniqueId().toString();

            player.sendMessage(Component.text("Game Stats:").color(Palette.LIME));
            TentoriumCore.modes.forEach((id, mode) -> {
                if (mode instanceof TrackedScore ts) {
                    player.sendMessage(Component.text(": ").color(Palette.GRAY).append(mode.name())
                            .append(Component.text(" - ").color(Palette.GRAY))
                            .append(Component.text(db.fetchInt(uuid, ts.leaderboardStatId())+ " "+ts.leaderboardStatName()).color(Palette.WHITE))
                    );
                }
            });
        }
    }
}
