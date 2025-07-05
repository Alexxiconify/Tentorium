package net.sylviameows.tentorium;

import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.sylviameows.tentorium.commands.*;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.database.SQLite;
import net.sylviameows.tentorium.leaderboards.LeaderboardManager;
import net.sylviameows.tentorium.leaderboards.LeaderboardTask;
import net.sylviameows.tentorium.modes.BaseModes.Mode;
import net.sylviameows.tentorium.modes.Parkour;
import net.sylviameows.tentorium.modes.FFAModes.KitFFA;
import net.sylviameows.tentorium.modes.FFAModes.KnockbackFFA;
import net.sylviameows.tentorium.modes.SpleefModes.ClassicSpleef;
import net.sylviameows.tentorium.modes.SpleefModes.TNTSpleef;
import org.bukkit.NamespacedKey;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class TentoriumCore extends JavaPlugin {
    private static ComponentLogger LOGGER;
    private static TentoriumCore INSTANCE;
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
        var pm = getServer().getPluginManager();
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

        // Register commands using Paper command system
        logger().info("Registering commands...");
        registerCommand("join", new GameCommands.JoinCommand());
        registerCommand("leave", new GameCommands.LeaveCommand());
        registerCommand("modes", new GameCommands.ModesCommand());
        registerCommand("kit", new GameCommands.KitCommand());
        registerCommand("select", new GameCommands.SelectCommand());
        registerCommand("bypass", new GameCommands.BypassCommand());
        registerCommand("setleaderboard", new GameCommands.SetLeaderboardCommand());
        registerCommand("leaderboard", new GameCommands.LeaderboardCommand());
        registerCommand("stats", new GameCommands.StatsCommand());
        
        // Register mode-specific commands
        for (Mode mode : modes) {
            registerCommand(mode.id(), new GameCommands.ModeAlias(mode.id()));
        }
        logger().info("Commands registered!");

        // Initialize configuration
        new Config(this);

        // Initialize leaderboards
        logger().info("Initializing leaderboards...");
        LEADERBOARD = new LeaderboardManager(this);
        new LeaderboardTask(this);
        logger().info("Leaderboards initialized!");

        logger().info("Tentorium plugin enabled successfully!");
    }

    private void registerCommand(String name, Object executor) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor((org.bukkit.command.CommandExecutor) executor);
            if (executor instanceof org.bukkit.command.TabCompleter) {
                command.setTabCompleter((org.bukkit.command.TabCompleter) executor);
            }
        }
    }

    @Override
    public void onDisable() {
        logger().info("Tentorium plugin disabled!");
    }
}
