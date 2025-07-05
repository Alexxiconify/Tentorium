package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModeAlias implements BasicCommand {
    private final String mode;

    public ModeAlias(String mode) {
        this.mode = mode;
    }

    @Override
    public void execute(CommandSourceStack source, String[] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player){
            var mode = TentoriumCore.modes.get(this.mode);
            mode.join(player);
        } else {
            target.sendMessage("You must be a player to run this command.");
        }
    }
}
