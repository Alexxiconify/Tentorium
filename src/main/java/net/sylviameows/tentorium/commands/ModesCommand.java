package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.gui.spawn.ModeSelectionGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModesCommand implements BasicCommand {
    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player) {
            var gui = new ModeSelectionGUI();
            player.openInventory(gui.getInventory());
        }

    }
}
