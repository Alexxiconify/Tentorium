package net.sylviameows.tentorium.modes.ffa;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.gui.ffa.KitSelectionGUI;
import net.sylviameows.tentorium.modes.ffa.kits.ArcherKit;
import net.sylviameows.tentorium.modes.ffa.kits.Kit;
import net.sylviameows.tentorium.modes.ffa.kits.KnightKit;
import net.sylviameows.tentorium.modes.ffa.kits.RoyalKit;
import net.sylviameows.tentorium.utilities.Area;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KitFFA extends FFA {
    private static final HashMap<String, Kit> kits = new HashMap<>();
    private final ArrayList<Location> player_placed_blocks = new ArrayList<>();

    private final ArrayList<Player> fighting = new ArrayList<>();

    public static HashMap<String, Kit> kits() {
        return kits;
    }

    public Location spawn() {
        return getOptions().location();
    }
    public Area lobby() {
        return getOptions().region();
    }

    @Override
    public Component name() {
        return Component.text("Free For All").color(Palette.LIME);
    }

    public KitFFA() {
        Kit[] kits = {
                new KnightKit(),
                new RoyalKit(),
                new ArcherKit()
        };

        for (Kit kit : kits) {
            KitFFA.kits.put(kit.id(), kit);
        }
    }

    @Override
    public ModeConfig getOptions() {
        if (options != null) return options;
        var options = Config.get().getSerializable("ffa", ModeConfig.class);
        this.options = options;
        return options;
    }

    @Override
    public void join(Player player) {
        super.join(player);
        respawn(player);
    }

    @Override
    public void leave(Player player) {
        super.leave(player);

        fighting.remove(player);
    }

    @Override
    protected void respawn(Player player) {
        super.respawn(player);

        var tp = player.teleportAsync(spawn());
        tp.whenComplete((result, ex) -> fighting.remove(player));

        player.clearActivePotionEffects();
    }

    @Override
    public void giveKit(Player player) {
        var settings = PlayerManager.get(player).ffa();

        if (settings.kit().equals("shuffle")) {
            var all = new ArrayList<>(kits.values().stream().toList());
            Collections.shuffle(all);
            all.getFirst().apply(player);
            giveSelector(player);
            return;
        }

        Kit kit = kits.get(settings.kit());
        kit.apply(player);
        giveSelector(player);
    }

    private void giveSelector(Player player) {
        var item = new ItemStack(Material.CHEST_MINECART);
        item.editMeta(meta -> {
            meta.displayName(Component.text("Select Kit").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                    .color(Palette.LIME));
            meta.getPersistentDataContainer().set(
                    TentoriumCore.identififer("select_kit"),
                    PersistentDataType.BOOLEAN,
                    true
            );
        });
        player.getInventory().setItem(4, item);
    }

    @Override
    public String id() {
        return "ffa";
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && players.contains(player)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true);

            if (lobby().contains(player.getLocation())) event.setCancelled(true);
            var source_entity = event.getDamageSource().getCausingEntity();
            if (source_entity != null && lobby().contains(source_entity.getLocation())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void placeBlock(BlockPlaceEvent event) {
        if (!players.contains(event.getPlayer())) return;

        var location = event.getBlock().getLocation();
        if (lobby().contains(location)) {
            event.setCancelled(true);
            return;
        }

        player_placed_blocks.add(location);

        // leftover cobweb logic from the deprecated SpiderKit
        if (event.getBlock().getType() == Material.COBWEB) {
            Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), () -> {
                if (player_placed_blocks.contains(location)) {
                    player_placed_blocks.remove(location);
                    location.getBlock().setType(Material.AIR);
                }
            }, 12 * 20);
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR && event.getHand() == EquipmentSlot.HAND) {
            if (!event.getItem().getPersistentDataContainer().getOrDefault(TentoriumCore.identififer("select_kit"), PersistentDataType.BOOLEAN, false)) return;

            var gui = new KitSelectionGUI();
            event.getPlayer().openInventory(gui.getInventory());
        }
    }

    @Override @EventHandler
    public void breakBlock(BlockBreakEvent event) {
        super.breakBlock(event);
        var location = event.getBlock().getLocation();

        // uncancel the event and allow breaking of blocks players have placed in the game
        if (player_placed_blocks.contains(location)) {
            event.setCancelled(false);
            player_placed_blocks.remove(location);
        }
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player player) || !players.contains(player)) return;

        // keep golden apple regen the same
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) return;
        event.setAmount(event.getAmount() / 2);
    }

    @Override
    protected void rewardKiller(Player killer) {
        super.rewardKiller(killer);
        killer.getInventory().addItem(ItemUtilities.createItem(Material.GOLDEN_APPLE));

        var uuid = killer.getUniqueId().toString();
        var score = database().fetchInt(uuid, "ffa_kills");
        database().update(uuid, "ffa_kills", score+1);
    }

    @Override @EventHandler
    public void move(PlayerMoveEvent event) {
        super.move(event);

        var player = event.getPlayer();
        if (!players.contains(player)) return;

        if (fighting.contains(player)) return;
        if (lobby().contains(player.getLocation())) return;

        fighting.add(player);

        var inventory = player.getInventory();
        try {
            int slot = inventory.first(Material.CHEST_MINECART);
            inventory.clear(slot);
        } catch (Exception ignored) {

        }
    }

    @Override
    public String leaderboardStatId() {
        return "ffa_kills";
    }
}
