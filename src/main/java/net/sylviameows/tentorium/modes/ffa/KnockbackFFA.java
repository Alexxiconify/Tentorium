package net.sylviameows.tentorium.modes.ffa;

import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.utilities.Area;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class KnockbackFFA extends FFA {
    private final Material WEAPON_MATERIAL = Material.STICK;

    @Override
    protected void respawn(Player player) {
        super.respawn(player);

        player.teleportAsync(spawn());
        player.clearActivePotionEffects();
//        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 4 * 20, 0, true, false, true));
    }

    @Override
    public Component name() {
        return Component.text("Knockback").color(Palette.PURPLE_LIGHT);
    }

    @Override
    public String id() {
        return "knockback";
    }

    @Override
    public ModeConfig getOptions() {
        if (options != null) return options;
        var options = Config.get().getSerializable("knockback", ModeConfig.class);
        this.options = options;
        return options;
    }

    @Override
    public void join(Player player) {
        super.join(player);
        respawn(player);
    }

    @Override
    void giveKit(Player player) {
        var inventory = player.getInventory();
        inventory.clear();

        var item = new ItemStack(WEAPON_MATERIAL, 1);
        item.editMeta(meta -> {
            meta.setUnbreakable(true);
            meta.setFood(null);
            meta.addEnchant(Enchantment.KNOCKBACK, 2, true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        });


        inventory.setItem(0, item);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && players.contains(player)) {
            if (lobby().contains(player.getLocation()) || event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
                return;
            }


            if (event.getDamageSource().getDamageType() == DamageType.GENERIC_KILL) return;
            if (player.getLocation().y() <= voidLevel()) {
                return;
            }
            event.setDamage(0.0D);
        }
    }

    @Override
    protected void rewardKiller(Player killer) {
        super.rewardKiller(killer);

        var uuid = killer.getUniqueId().toString();
        var score = database().fetchInt(uuid, "kb_kills");
        database().update(uuid, "kb_kills", score+1);
    }

    @Override
    public String leaderboardStatId() {
        return "kb_kills";
    }
}
