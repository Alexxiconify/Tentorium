package net.sylviameows.tentorium;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.sylviameows.tentorium.commands.*;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.database.SQLite;
import net.sylviameows.tentorium.leaderboards.LeaderboardManager;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.modes.Parkour;
import net.sylviameows.tentorium.modes.ffa.KitFFA;
import net.sylviameows.tentorium.modes.ffa.KnockbackFFA;
import net.sylviameows.tentorium.modes.spleef.ClassicSpleef;
import net.sylviameows.tentorium.modes.spleef.TNTSpleef;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public class TentoriumCore extends JavaPlugin {
    private static ComponentLogger LOGGER;
    private static TentoriumCore INSTANCE;
    private static Config CONFIG;
    private static LeaderboardManager LEADERBOARD;
    private static SQLite DATABASE;

    public static ComponentLogger logger() {
        return LOGGER;
    }

    public static TentoriumCore instance() {
        return INSTANCE;
    }

    public static LeaderboardManager leaderboard() {
        return LEADERBOARD;
    }

    public static SQLite database() {
        return DATABASE;
    }

    public static NamespacedKey identififer(String value) {
        return new NamespacedKey("tentorium", value);
    }

    public static HashMap<String, Mode> modes = new HashMap<>();

    @Override
    public void onEnable() {
        // Initialize static instances
        INSTANCE = this;
        LOGGER = getComponentLogger();

        logger().info("Starting Tentorium plugin...");

        // Initialize database
        logger().info("Connecting to database...");
        DATABASE = new SQLite(this);
        DATABASE.load();

        // Register event listeners
        logger().info("Registering events...");
        Listener[] events = {
                new PlayerManager(),
                new InventoryListener(this),
                new SpawnListener()
        };

        int eventsLoaded = 0;
        var pm = Bukkit.getPluginManager();
        for (Listener listener : events) {
            pm.registerEvents(listener, this);
            eventsLoaded++;
        }
        logger().info("Loaded " + eventsLoaded + " event listener(s)!");

        // Load game modes
        logger().info("Loading game modes...");
        Mode[] modes = {
                new KnockbackFFA(),
                new KitFFA(),
                new ClassicSpleef(),
                new TNTSpleef(),
                new Parkour()
        };

        int modesLoaded = 0;
        for (Mode mode : modes) {
            TentoriumCore.modes.put(mode.id(), mode);
            pm.registerEvents(mode, this);
            modesLoaded++;
        }
        logger().info("Loaded " + modesLoaded + " game mode(s)!");

        // Register commands using Paper's command system
        var lem = this.getLifecycleManager();
        lem.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            
            // Main commands
            commands.register("join", "Join a specific game mode", List.of("j", "mode"), new JoinCommand());
            commands.register("leave", "Leave current game mode and return to spawn", List.of("spawn", "lobby", "hub", "l"), new LeaveCommand());
            commands.register("modes", "Open the mode selection GUI", new ModesCommand());
            
            // FFA commands
            commands.register("kit", "Select a kit for FFA mode", List.of("kits"), new KitCommand());
            
            // Parkour commands
            commands.register("select", "Select a parkour map", new SelectCommand());
            
            // Admin commands
            commands.register("bypass", "Toggle spawn protection bypass", new BypassCommand());
            commands.register("setleaderboard", "Set leaderboard location for a mode", List.of("slp"), new SetLeaderboardCommand());
            
            // Stats and leaderboard commands
            commands.register("leaderboard", "View leaderboards for each game", new LeaderboardCommand());
            commands.register("stats", "View stats for each game", new StatsCommand());
            
            // Register mode-specific commands
            for (Mode mode : modes) {
                commands.register(mode.id(), "Join " + mode.name().toString(), new ModeAlias(mode.id()));
            }
        });

        // Initialize configuration
        CONFIG = new Config(this);

        // Initialize leaderboards
        logger().info("Initializing leaderboards...");
        LEADERBOARD = new LeaderboardManager(this);
        logger().info("Leaderboards initialized!");

        logger().info("Tentorium plugin enabled successfully!");
    }

    @Override
    public void onDisable() {
        logger().info("Tentorium plugin disabled!");
    }
}
