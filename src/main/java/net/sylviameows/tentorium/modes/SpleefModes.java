package net.sylviameows.tentorium.modes;

import com.fastasyncworldedit.core.function.mask.BlockMaskBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockType;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.config.serializable.SpleefConfig;
import net.sylviameows.tentorium.config.serializable.spleef.ClassicFloors;
import net.sylviameows.tentorium.utilities.GameUtilities;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SpleefModes {
    // Base Spleef class
    public static abstract class Spleef extends BaseModes.Mode implements TrackedScore, ConfigurableMode {
        protected boolean active = false;
        protected ArrayList<Player> alive = new ArrayList<>();
        protected SpleefConfig options;
        
        public void reload() {
            options = null;
        }

        @Override
        public void join(Player player) {
            player.teleportAsync(spawn()).whenComplete((result, ex) -> {
                super.join(player);
            });
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
        }

        @Override
        public void leave(Player player) {
            super.leave(player);
            checkThenReset(player);
        }

        private void checkThenReset(Player player) {
            alive.remove(player);

            if (alive.size() <= 1) {
                if (alive.isEmpty()) {
                    reset();
                    return;
                }
                var winner = alive.getFirst();
                winner(winner);

                players.forEach(p -> {
                    p.sendMessage(winner.name().color(Palette.AQUA).append(Component.text(" won the match!").color(Palette.WHITE)));
                    p.showTitle(Title.title(
                            Component.text(winner.getName()).color(Palette.AQUA),
                            Component.text("won the match!").color(Palette.WHITE),
                            Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(2), Duration.ofMillis(500))
                    ));
                });

                Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), this::reset, 20L * 3);
            }
        }

        @EventHandler
        public void damage(EntityDamageEvent event) {
            if (event.getEntity() instanceof Player player && players.contains(player)) event.setCancelled(true);
        }

        @EventHandler
        public void move(PlayerMoveEvent event) {
            var player = event.getPlayer();
            if (players.contains(player) && !alive.contains(player)) {
                var player_location = player.getLocation();
                if (lobby().contains(player_location)) return;

                var start_vector = player_location.toVector();
                var end_vector = spawn().toVector();

                var difference_vector = end_vector.subtract(start_vector);

                if (!active) {
                    // join game

                    if (!alive.isEmpty()) countdown(15);
                    alive.add(player);

                    setupPlayer(player);

                    player.setVelocity(difference_vector.normalize().multiply(-1).add(new Vector(0, 0.3, 0)));

                    return;
                }

                // launch backwards

                player.setVelocity(difference_vector.normalize());
            } else if (alive.contains(player) && event.getTo().y() <= voidLevel()) {

                player.teleportAsync(spawn());
                player.getInventory().clear();
                checkThenReset(player);
                // todo: death message
            }
        }

        protected void setupPlayer(Player player) {}
        protected void winner(Player player) {
            var name = PlainTextComponentSerializer.plainText().serialize(name());
            player.sendMessage(Component.text("(+1 "+ name.toLowerCase() +" win)").color(Palette.GRAY));
            player.playSound(Sound.sound(Key.key("minecraft", "entity.arrow.hit_player"), Sound.Source.PLAYER, 0.3f, 0.9f));
        }

        protected void countdown(int seconds_left) {
            if (seconds_left <= 0) {
                players.forEach(player -> {
                    player.showTitle(Title.title(
                            Component.text("GO").color(Palette.LIME).decorate(TextDecoration.BOLD),
                            Component.empty(),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                    ));
                });

                active = true;
                return;
            }

            players.forEach(player -> {
                TextColor color = Palette.YELLOW;
                Sound sound = Sound.sound(Key.key("block.note_block.bit"), Sound.Source.PLAYER, 1f, 2f);
                if (seconds_left <= 3) {
                    player.showTitle(Title.title(
                            Component.text(seconds_left).color(color),
                            Component.empty(),
                            Title.Times.times(Duration.ZERO, Duration.ofSeconds(1), Duration.ofMillis(250))
                    ));
                } else {
                    player.sendActionBar(Component.text(seconds_left).color(color));
                }
                if (seconds_left < 10) player.playSound(sound);
            });

            Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), () -> countdown(seconds_left -1), 20L);
        }

        protected void reset() {
            alive.forEach(player -> player.teleportAsync(spawn()));
            alive.clear();
            active = false;

            refresh();
        }

        /**
         * Reset the map.
         */
        abstract protected void refresh();

        @Override
        public String leaderboardStatName() {
            return "wins";
        }
    }

    // ClassicSpleef class
    public static class ClassicSpleef extends Spleef {
        @Override
        public Component name() {
            return super.name().color(Palette.AQUA);
        }

        @Override
        public String id() {
            return "spleef";
        }

        @Override
        protected void setupPlayer(Player player) {
            var inventory = player.getInventory();
            inventory.clear();
            inventory.addItem(GameUtilities.ItemUtilities.createItem(Material.DIAMOND_SHOVEL, meta -> meta.addEnchant(Enchantment.EFFICIENCY, 5, true)));
        }

        @Override
        protected void refresh() {
            var world = BukkitAdapter.adapt(Bukkit.getWorld("world"));
            try (EditSession session = WorldEdit.getInstance().newEditSessionBuilder().world(world).build()) {
                var floors = getOptions().floors();

                int layers = ((ClassicFloors) floors).layers();
                int gap = floors.gap();

                int y = floors.y();

                Mask mask = new BlockMaskBuilder().add(BlockType.REGISTRY.get("minecraft:air")).build(session);
                for (int i = 0; i < layers; i++) {
                    var region = new CuboidRegion(
                            BlockVector3.at(floors.x1(), y, floors.z1()),
                            BlockVector3.at(floors.x2(), y, floors.z2())
                    );

                    try {
                        session.replaceBlocks(region, mask, BlockType.REGISTRY.get("minecraft:snow_block").getDefaultState());
                    } catch (MaxChangedBlocksException e) {
                        TentoriumCore.logger().warn("Failed to replace blocks in Classic Spleef: " + e.getMessage());
                    }
                    y = y - gap - 1 /* adds the one block we set to make it not wrong or whatever */;
                }
            }
        }

        @EventHandler
        public void breakBlock(BlockBreakEvent event) {
            var player = event.getPlayer();
            if (!active && players.contains(player)) {
                event.setCancelled(true);
            } else if (players.contains(player) && !alive.contains(player)) {
                event.setCancelled(true);
            } else if (alive.contains(player) && event.getBlock().getType() != Material.SNOW_BLOCK) {
                event.setCancelled(true);
            }
        }

        @Override
        protected void winner(Player player) {
            super.winner(player);

            var uuid = player.getUniqueId().toString();
            var score = database().fetchInt(uuid, "spleef_wins");
            database().update(uuid, "spleef_wins", score+1);
        }

        @Override
        public String leaderboardStatId() {
            return "spleef_wins";
        }

        @Override
        public SpleefConfig getOptions() {
            if (options != null) return options;
            var options = Config.get().getSerializable("spleef", SpleefConfig.class);
            this.options = options;
            return options;
        }
    }

    // TNTSpleef class
    public static class TNTSpleef extends Spleef {
        public TNTSpleef() {
            Bukkit.getScheduler().runTaskTimer(TentoriumCore.instance(), this::tick, 1L, 1L);
        }

        private void tick() {
            if (!active) return;
            alive.forEach(player -> {
                if (player == null || !player.isOnline()) return;
                
                var world = player.getWorld();
                if (world == null) return;
                
                var bounding_box = player.getBoundingBox();

                var center = new Location(world, bounding_box.getCenterX(), bounding_box.getMinY(), bounding_box.getCenterZ());
                center.add(0, -0.5, 0);
                var center_block = world.getBlockAt(center);

                if (center_block.getType().hasGravity()) {
                    Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), () -> {
                        if (center_block.getType().hasGravity()) {
                            center_block.setType(Material.AIR); // break block
                            var tntLocation = center_block.getLocation().add(0, -1, 0);
                            world.getBlockAt(tntLocation).setType(Material.AIR); // break tnt
                        }
                    }, 4L);
                    return;
                }

                // backup detection
                var min = new Location(world, bounding_box.getMinX(), bounding_box.getMinY(), bounding_box.getMinZ());

                Location[] corners = new Location[4];
                corners[0] = min.clone().add(0, -0.5, 0);
                corners[1] = min.clone().add(bounding_box.getWidthX(), 0, 0);
                corners[2] = min.clone().add(0, 0, bounding_box.getWidthZ());
                corners[3] = min.clone().add(bounding_box.getWidthX(), 0, bounding_box.getWidthZ());

                for (Location location : corners) {
                    var block = world.getBlockAt(location);

                    if (block.getType().hasGravity()) {
                        Bukkit.getScheduler().runTaskLater(TentoriumCore.instance(), () -> {
                            if (block.getType().hasGravity()) {
                                block.setType(Material.AIR); // break block
                                var tntLocation = location.clone().add(0, -1, 0);
                                world.getBlockAt(tntLocation).setType(Material.AIR); // break tnt
                            }
                        }, 4L);
                    }
                }
            });
        }

        @Override
        public Component name() {
            return MiniMessage.miniMessage().deserialize("<rainbow>Rainbow Rumble</rainbow>");
        }

        @Override
        public String id() {
            return "tntrun";
        }

        @Override
        public SpleefConfig getOptions() {
            if (options != null) return options;
            var options = Config.get().getSerializable("tntrun", SpleefConfig.class);
            this.options = options;
            return options;
        }

        @Override
        protected void refresh() {
            var world = BukkitAdapter.adapt(Bukkit.getWorld("world"));
            try (EditSession session = WorldEdit.getInstance().newEditSessionBuilder().world(world).build()) {
                var floors = getOptions().floors();
                List<BlockType> layers = new ArrayList<>();

                for (String material : ((net.sylviameows.tentorium.config.serializable.spleef.TNTFloors) getOptions().floors()).layers()) {
                    layers.add(BlockType.REGISTRY.get(material));
                }

                int gap = floors.gap();
                int y = floors.y();

                Mask mask = new BlockMaskBuilder().add(BlockType.REGISTRY.get("minecraft:air")).build(session);
                for (int i = 0; i < layers.size(); i++) {
                    var block_region = new CuboidRegion(
                            BlockVector3.at(floors.x1(), y, floors.z1()),
                            BlockVector3.at(floors.x2(), y, floors.z2())
                    );

                    var tnt_region = new CuboidRegion(
                            BlockVector3.at(floors.x1(), y-1, floors.z1()),
                            BlockVector3.at(floors.x2(), y-1, floors.z2())
                    );

                    try {
                        session.replaceBlocks(tnt_region, mask, BlockType.REGISTRY.get("minecraft:tnt").getDefaultState());
                        session.replaceBlocks(block_region, mask, layers.get(i).getDefaultState());
                    } catch (MaxChangedBlocksException e) {
                        TentoriumCore.logger().warn("Failed to replace blocks in TNT Spleef: " + e.getMessage());
                    }
                    y = y - gap - 2 /* adds the tnt and following block we set to make it not wrong or whatever */;
                }
            }
        }

        @EventHandler
        public void breakBlock(BlockBreakEvent event) {
            if (!players.contains(event.getPlayer())) return;

            event.setCancelled(true);
        }

        @Override
        protected void winner(Player player) {
            super.winner(player);

            var uuid = player.getUniqueId().toString();
            var score = database().fetchInt(uuid, "tnt_wins");
            database().update(uuid, "tnt_wins", score+1);
        }

        @Override
        public String leaderboardStatId() {
            return "tnt_wins";
        }
    }
} 