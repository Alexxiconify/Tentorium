package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.gui.ffa.KitSelectionGUI;
import net.sylviameows.tentorium.modes.ffa.KitFFA;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class KitCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player){
            var settings = PlayerManager.get(player);

            if (settings.mode() instanceof KitFFA) {
                if (args.length < 1) {
                    var gui = new KitSelectionGUI();
                    player.openInventory(gui.getInventory());
                    return;
                }

                var kit = args[0].toLowerCase();
                if (KitFFA.kits().containsKey(kit) || kit.equals("shuffle")) {
                    settings.ffa().kit(kit);
                } else {
                    player.sendMessage("The kit you entered is not a valid kit.");
                }

            } else {
                player.sendMessage("The gamemode you are in does not support kits.");
            }
        } else {
            target.sendMessage("You must be a player to run this command.");
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        if (args.length <= 1) {
            Collection<String> suggestions = new ArrayList<>();
            KitFFA.kits().forEach((id,_kit) -> {
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
