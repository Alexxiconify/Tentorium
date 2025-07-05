package net.sylviameows.tentorium.modes.spleef;

import com.fastasyncworldedit.core.function.mask.BlockMaskBuilder;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.block.BlockType;
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.config.serializable.ModeConfig;
import net.sylviameows.tentorium.config.serializable.SpleefConfig;
import net.sylviameows.tentorium.config.serializable.spleef.ClassicFloors;
import net.sylviameows.tentorium.utilities.Area;
import net.sylviameows.tentorium.utilities.ItemUtilities;
import net.sylviameows.tentorium.utilities.Palette;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class ClassicSpleef extends Spleef {
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
        inventory.addItem(ItemUtilities.createItem(Material.DIAMOND_SHOVEL, meta -> meta.addEnchant(Enchantment.EFFICIENCY, 5, true)));
    }

    @Override
    protected void refresh() {
        var world = BukkitAdapter.adapt(Bukkit.getWorld("world"));
        try (EditSession session = WorldEdit.getInstance().newEditSessionBuilder().world(world).limitUnlimited().build()) {
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

                session.replaceBlocks(region, mask, BlockType.REGISTRY.get("minecraft:snow_block"));
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
