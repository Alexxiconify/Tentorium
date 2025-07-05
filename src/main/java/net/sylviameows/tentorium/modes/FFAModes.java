package net.sylviameows.tentorium.modes;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.gui.GameModeGUIs;
import net.sylviameows.tentorium.utilities.GameUtilities;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import net.sylviameows.tentorium.modes.BaseModes.TrackedScore;
import net.sylviameows.tentorium.modes.BaseModes.ConfigurableMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class FFAModes {
    // FFA base class
    public static abstract class FFA extends BaseModes.Mode implements TrackedScore, ConfigurableMode {
        protected ModeConfig options;
        public void reload() {
            options = null;
        }

        @EventHandler
        public void playerDeath(PlayerDeathEvent event) {
            var victim = event.getPlayer();
            if (!players.contains(victim)) return;
            var damageSource = victim.getLastDamageCause();
            if (damageSource != null) {
                var source = damageSource.getDamageSource().getCausingEntity();
                if (source instanceof Player killer && damageSource.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD && killer != victim) {
                    killer.sendMessage(MiniMessage.miniMessage().deserialize("<gray>◎ <white>You killed <yellow>"+victim.getName()+"</yellow></white> (+1)"));
                    rewardKiller(killer);

                    victim.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You were killed by <red>"+killer.getName()+"</red>!"));
                    demeritVictim(victim);
                } else if (damageSource.getDamageSource().getDamageType() != DamageType.OUT_OF_WORLD) {
                    victim.sendMessage(MiniMessage.miniMessage().deserialize("<gray>☠ <white>You died!"));
                    demeritVictim(victim);
                }
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
        }

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

    // KitFFA class
    public static class KitFFA extends FFA {
        private static final HashMap<String, GameKits.Kit> kits = new HashMap<>();
        private final ArrayList<Location> player_placed_blocks = new ArrayList<>();
        private final ArrayList<Player> fighting = new ArrayList<>();

        public static HashMap<String, GameKits.Kit> kits() {
            return kits;
        }

        public Location spawn() {
            return getOptions().location();
        }
        public GameUtilities.Area lobby() {
            return getOptions().region();
        }

        @Override
        public Component name() {
            return Component.text("Free For All").color(GameUtilities.Palette.LIME);
        }

        public KitFFA() {
            GameKits.Kit[] kits = {
                    new GameKits.KnightKit(),
                    new GameKits.RoyalKit(),
                    new GameKits.ArcherKit()
            };

            for (GameKits.Kit kit : kits) {
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
            tp.whenComplete((result, ex) -> {
                if (ex != null) {
                    TentoriumCore.logger().warn("Failed to teleport player " + player.getName() + " to spawn: " + ex.getMessage());
                }
                fighting.remove(player);
            });

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

            GameKits.Kit kit = kits.get(settings.kit());
            if (kit != null) {
                kit.apply(player);
            }
            giveSelector(player);
        }

        private void giveSelector(Player player) {
            var item = new ItemStack(Material.CHEST_MINECART);
            item.editMeta(meta -> {
                meta.displayName(Component.text("Select Kit").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
                        .color(GameUtilities.Palette.LIME));
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
                var item = event.getItem();
                if (item != null && item.getItemMeta() != null) {
                    if (!item.getItemMeta().getPersistentDataContainer().getOrDefault(TentoriumCore.identififer("select_kit"), PersistentDataType.BOOLEAN, false)) return;
                } else {
                    return;
                }

                var gui = new GameModeGUIs.KitSelectionGUI();
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
            killer.getInventory().addItem(GameUtilities.ItemUtilities.createItem(Material.GOLDEN_APPLE));

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
            int slot = inventory.first(Material.CHEST_MINECART);
            if (slot != -1) {
                inventory.clear(slot);
            }
        }

        @Override
        public String leaderboardStatId() {
            return "ffa_kills";
        }
    }

    // KnockbackFFA class
    public static class KnockbackFFA extends FFA {
        private final Material WEAPON_MATERIAL = Material.STICK;

        @Override
        protected void respawn(Player player) {
            super.respawn(player);

            player.teleportAsync(spawn()).whenComplete((result, ex) -> {
                if (ex != null) {
                    TentoriumCore.logger().warn("Failed to teleport player " + player.getName() + " to spawn: " + ex.getMessage());
                }
            });
            player.clearActivePotionEffects();
        }

        @Override
        public Component name() {
            return Component.text("Knockback").color(GameUtilities.Palette.PURPLE_LIGHT);
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
} 