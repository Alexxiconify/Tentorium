package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.modes.Parkour;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SelectCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player){
            var settings = PlayerManager.get(player);

            if (settings.mode() instanceof Parkour parkour) {
                if (args.length < 1) {
//                    var gui = new KitSelectionGUI();
//                    player.openInventory(gui.getInventory());

                    // todo: gui?
                    return;
                }

                var map = args[0].toLowerCase();
                if (parkour.maps().containsKey(map)) {
                    settings.parkour().map(map);
                    var location = parkour.maps().get(map);
                    settings.parkour().checkpoint(location);
                    player.teleportAsync(location);
                } else {
                    player.sendMessage("The map you entered is not a valid map.");
                }

            } else {
                player.sendMessage("The mode you are in does not support map selection.");
            }
        } else {
            target.sendMessage("You must be a player to run this command.");
        }
    }

    @Override
    public Collection<String> suggest(CommandSourceStack source, String[] args) {
        if (args.length <= 1) {
            Collection<String> suggestions = new ArrayList<>();
            Parkour.maps().forEach((id,_kit) -> {
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
