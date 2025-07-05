package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class JoinCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (args.length < 1) {
            target.sendMessage("Not enough arguments");
        } else if (target instanceof Player player){
            var mode = TentoriumCore.modes.get(args[0]);
            mode.join(player);
        } else {
            target.sendMessage("You must be a player to run this command.");
        }
    }

    @Override
    public Collection<String> suggest(@NotNull CommandSourceStack source, String @NotNull [] args) {
        if (args.length <= 1) {
            Collection<String> suggestions = new ArrayList<>();
            TentoriumCore.modes.forEach((id,_mode) -> {
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
