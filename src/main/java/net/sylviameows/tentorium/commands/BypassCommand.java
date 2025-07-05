package net.sylviameows.tentorium.commands;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.modes.Bypass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class BypassCommand implements BasicCommand {
    @Override
    public void execute(@NotNull CommandSourceStack source, String @NotNull [] args) {
        CommandSender target = source.getExecutor();
        if (target == null) target = source.getSender();

        if (target instanceof Player player) {
            var settings = PlayerManager.get(player);
            if (settings.mode() instanceof Bypass mode) {
                mode.leave(player);
                settings.mode(null);
                player.sendMessage("Disabled protection bypass.");
            } else {
                if (settings.mode() != null) settings.mode().leave(player);
                settings.mode(new Bypass());
                player.sendMessage("Enabled protection bypass.");
            }
        }
    }

    @Override
    public @Nullable String permission() {
        return "tentorium.bypass";
    }
}
