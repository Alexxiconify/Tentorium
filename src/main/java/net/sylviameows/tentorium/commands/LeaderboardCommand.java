package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.database.LeaderboardResponse;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

public class LeaderboardCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (args.length < 1) {
            target.sendMessage("Provide a mode argument to view a leaderboard.");
        } else {
            String mode_name = args[0];
            Mode mode = TentoriumCore.modes.get(mode_name);

            if (mode instanceof TrackedScore tracked) {

                LeaderboardResponse response;
                if (target instanceof Player player) {
                    response = tracked.getLeaderboard(player);
                } else {
                    response = tracked.getLeaderboard();
                }

                AtomicReference<Component> reference = new AtomicReference<>(Component.text("Leaderboard for "));
                reference.getAndUpdate(component -> component.append(mode.name()));

                response.forEach((place, lp) -> {
                    if (lp == null) return;
                    reference.getAndUpdate(component -> component.appendNewline());

                    var line = Component.text(place+". "+lp.name()).color(Palette.WHITE)
                            .append(Component.text(" - ").color(Palette.GRAY))
                            .append(Component.text(lp.score()+" "+tracked.leaderboardStatName()).color(Palette.WHITE));
                    reference.getAndUpdate(component -> component.append(line));
                });

                target.sendMessage(reference.get());
            } else {
                target.sendMessage("Mode does not have trackable stats.");
            }


        }
    }

    @Override
    public Collection<String> suggest(@NotNull CommandSourceStack source, String @NotNull [] args) {
        if (args.length <= 1) {
            Collection<String> suggestions = new ArrayList<>();
            TentoriumCore.modes.forEach((id,mode) -> {
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
