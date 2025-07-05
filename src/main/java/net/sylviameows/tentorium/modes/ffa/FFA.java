package net.sylviameows.tentorium.modes.ffa;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.config.serializable.SpleefConfig;
import net.sylviameows.tentorium.modes.ConfigurableMode;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.utilities.Area;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class FFA extends Mode implements TrackedScore, ConfigurableMode {
    protected ModeConfig options;
    public void reload() {
        options = null;
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        var victim = event.getPlayer();
        if (!players.contains(victim)) return;
        var source = event.getDamageSource().getCausingEntity();
        if (source instanceof Player killer && event.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD && killer != victim) {
            killer.sendMessage(MiniMessage.miniMessage().deserialize("<gray>◎ <white>You killed <yellow>"+victim.getName()+"</yellow></white> (+1)"));
            rewardKiller(killer);

            victim.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You were killed by <red>"+killer.getName()+"</red>!"));
            demeritVictim(victim);
        } else if (event.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD) {
            victim.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You died!"));
            demeritVictim(victim);
        }

        victim.setHealth(20.0);
        event.setCancelled(true);
        respawn(victim);
    }

    @Override
    public void join(Player player) {
        super.join(player);
    }

    protected void respawn(Player player) {
        giveKit(player);

        player.setFoodLevel(20);
        player.setSaturation(10);
    };

    abstract void giveKit(Player player);

    @EventHandler
    public void move(PlayerMoveEvent event) {
        var player = event.getPlayer();
        if (players.contains(player)) {
            if (event.getFrom().y() <= voidLevel()) {
                var cause = player.getLastDamageCause();
                if (cause != null && cause.getDamageSource().getCausingEntity() instanceof Player killer && killer != player) {
                    killer.sendMessage(MiniMessage.miniMessage().deserialize("<gray>◎ <white>You sent <yellow>"+player.getName()+"</yellow> into the void!</white> (+1)"));
                    rewardKiller(killer);
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You were sent to the void by <red>"+killer.getName()+"</red>!"));
                    demeritVictim(player);
                } else {
                    player.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You fell into the void!"));;
                    demeritVictim(player);
                }
                player.damage(200.0D, DamageSource.builder(DamageType.OUT_OF_WORLD).build());
            }
        }
    }

    @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        if (!players.contains(event.getPlayer())) return;

        event.setCancelled(true);
    }

    protected void rewardKiller(Player killer) {
        killer.playSound(Sound.sound(Key.key("minecraft", "entity.arrow.hit_player"), Sound.Source.PLAYER, 0.3f, 0.9f));
    }

    protected void demeritVictim(Player victim) {

    }

    @Override
    public String leaderboardStatName() {
        return "kills";
    }
}
