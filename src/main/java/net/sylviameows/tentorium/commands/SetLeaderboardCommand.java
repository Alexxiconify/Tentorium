package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.database.LeaderboardResponse;
import net.sylviameows.tentorium.modes.Bypass;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class SetLeaderboardCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player) {
            var location = player.getLocation();
            if (args.length < 1) return;
            String mode_name = args[0];
            Mode mode = TentoriumCore.modes.get(mode_name);

            if (mode instanceof TrackedScore) {
                location.setPitch(0);
                location.setYaw(0);
                TentoriumCore.leaderboard().put(mode, location);
            } else {
                target.sendMessage("Mode does not have trackable stats.");
            }
        }
    }

    @Override
    public @Nullable String permission() {
        return "tentorium.set_leaderboard";
    }

    @Override
    public Collection<String> suggest(@NotNull CommandSourceStack source, String @NotNull [] args) {
        if (args.length <= 1) {
            Collection<String> suggestions = new ArrayList<>();
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
