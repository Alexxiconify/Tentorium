package net.sylviameows.tentorium.commands;

// All necessary imports for all commands
import net.kyori.adventure.text.Component;
import net.sylviameows.tentorium.PlayerManager;
import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.database.LeaderboardResponse;
import net.sylviameows.tentorium.modes.Bypass;
import net.sylviameows.tentorium.modes.Mode;
import net.sylviameows.tentorium.modes.Parkour;
import net.sylviameows.tentorium.modes.TrackedScore;
import net.sylviameows.tentorium.modes.ffa.KitFFA;
import net.sylviameows.tentorium.utilities.Palette;
import net.sylviameows.tentorium.values.Spawn;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static net.sylviameows.tentorium.SpawnListener.giveSelector;

public class GameCommands {
    // JoinCommand
    public static class JoinCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (args.length < 1) {
                sender.sendMessage("Usage: /join <mode>");
                return true;
            }
            if (sender instanceof Player player) {
                String modeName = args[0];
                if (modeName == null || modeName.isEmpty()) {
                    player.sendMessage("Unknown mode: " + modeName);
                    return true;
                }
                var mode = TentoriumCore.modes.get(modeName);
                if (mode != null) {
                    mode.join(player);
                } else {
                    player.sendMessage("Unknown mode: " + modeName);
                }
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            if (args.length <= 1) {
                List<String> suggestions = new ArrayList<>();
                TentoriumCore.modes.forEach((id, _mode) -> suggestions.add(id));
                if (args.length == 1) {
                    return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
                }
                return suggestions;
            } else {
                return Collections.emptyList();
            }
        }
    }

    // LeaveCommand
    public static class LeaveCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var settings = PlayerManager.get(player);
                if (settings.mode() != null) {
                    settings.mode().leave(player);
                    settings.mode(null);
                }
                var inventory = player.getInventory();
                inventory.clear();
                player.clearActivePotionEffects();
                player.teleportAsync(Spawn.LOCATION);
                giveSelector(inventory);
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
    }

    // KitCommand
    public static class KitCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var settings = PlayerManager.get(player);
                if (settings.mode() instanceof KitFFA) {
                    if (args.length < 1) {
                        var gui = new net.sylviameows.tentorium.gui.GameModeGUIs.KitSelectionGUI();
                        player.openInventory(gui.getInventory());
                        return true;
                    }
                    String kit = args[0];
                    if (kit == null || kit.isEmpty()) {
                        player.sendMessage("Usage: /kit <kit_name>");
                        return true;
                    }
                    kit = kit.toLowerCase();
                    if (KitFFA.kits().containsKey(kit) || kit.equals("shuffle")) {
                        settings.ffa().kit(kit);
                        player.sendMessage("Kit selected: " + kit);
                    } else {
                        player.sendMessage("The kit you entered is not a valid kit.");
                    }
                } else {
                    player.sendMessage("The gamemode you are in does not support kits.");
                }
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            if (args.length <= 1) {
                List<String> suggestions = new ArrayList<>();
                KitFFA.kits().forEach((id, _kit) -> suggestions.add(id));
                suggestions.add("shuffle");
                if (args.length == 1) {
                    return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
                }
                return suggestions;
            } else {
                return Collections.emptyList();
            }
        }
    }

    // SelectCommand
    public static class SelectCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var settings = PlayerManager.get(player);
                if (settings.mode() instanceof Parkour) {
                    if (args.length < 1) {
                        player.sendMessage("Usage: /select <map>");
                        return true;
                    }
                    String map = args[0];
                    if (map == null || map.isEmpty()) {
                        player.sendMessage("Usage: /select <map>");
                        return true;
                    }
                    map = map.toLowerCase();
                    if (Parkour.maps().containsKey(map)) {
                        settings.parkour().map(map);
                        var location = Parkour.maps().get(map);
                        settings.parkour().checkpoint(location);
                        player.teleportAsync(location);
                        player.sendMessage("Selected map: " + map);
                    } else {
                        player.sendMessage("The map you entered is not a valid map.");
                    }
                } else {
                    player.sendMessage("The mode you are in does not support map selection.");
                }
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            if (args.length <= 1) {
                List<String> suggestions = new ArrayList<>();
                Parkour.maps().forEach((id, _kit) -> suggestions.add(id));
                if (args.length == 1) {
                    return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
                }
                return suggestions;
            } else {
                return Collections.emptyList();
            }
        }
    }

    // ModesCommand
    public static class ModesCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var gui = new net.sylviameows.tentorium.gui.GameModeGUIs.ModeSelectionGUI();
                player.openInventory(gui.getInventory());
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
    }

    // ModeAlias
    public static class ModeAlias implements CommandExecutor {
        private final String mode;
        public ModeAlias(String mode) {
            this.mode = mode;
        }
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var mode = TentoriumCore.modes.get(this.mode);
                if (mode != null) {
                    mode.join(player);
                } else {
                    player.sendMessage("Unknown mode: " + this.mode);
                }
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
    }

    // BypassCommand
    public static class BypassCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
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
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            return Collections.emptyList();
        }
    }

    // StatsCommand
    public static class StatsCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var db = TentoriumCore.database();
                var uuid = player.getUniqueId().toString();
                player.sendMessage(Component.text("Game Stats:").color(Palette.LIME));
                TentoriumCore.modes.forEach((id, mode) -> {
                    if (mode instanceof TrackedScore ts) {
                        player.sendMessage(Component.text(": ").color(Palette.GRAY).append(mode.name())
                                .append(Component.text(" - ").color(Palette.GRAY))
                                .append(Component.text(db.fetchInt(uuid, ts.leaderboardStatId()) + " " + ts.leaderboardStatName()).color(Palette.WHITE))
                        );
                    }
                });
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
    }

    // LeaderboardCommand
    public static class LeaderboardCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (args.length < 1) {
                sender.sendMessage("Usage: /leaderboard <mode>");
                return true;
            }
            String modeName = args[0];
            if (modeName == null || modeName.isEmpty()) {
                sender.sendMessage("Usage: /leaderboard <mode>");
                return true;
            }
            Mode mode = TentoriumCore.modes.get(modeName);
            if (mode instanceof TrackedScore tracked) {
                LeaderboardResponse response;
                if (sender instanceof Player player) {
                    response = tracked.getLeaderboard(player);
                } else {
                    response = tracked.getLeaderboard();
                }
                AtomicReference<Component> reference = new AtomicReference<>(Component.text("Leaderboard for "));
                reference.getAndUpdate(component -> component.append(mode.name()));
                response.forEach((place, lp) -> {
                    if (lp == null) return;
                    reference.getAndUpdate(component -> component.appendNewline());
                    var line = Component.text(place + ". " + lp.name()).color(Palette.WHITE)
                            .append(Component.text(" - ").color(Palette.GRAY))
                            .append(Component.text(lp.score() + " " + tracked.leaderboardStatName()).color(Palette.WHITE));
                    reference.getAndUpdate(component -> component.append(line));
                });
                sender.sendMessage(reference.get());
            } else {
                sender.sendMessage("Mode does not have trackable stats.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            if (args.length <= 1) {
                List<String> suggestions = new ArrayList<>();
                TentoriumCore.modes.forEach((id, mode) -> {
                    if (!(mode instanceof TrackedScore)) return;
                    suggestions.add(id);
                });
                if (args.length == 1) {
                    return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
                }
                return suggestions;
            } else {
                return Collections.emptyList();
            }
        }
    }

    // SetLeaderboardCommand
    public static class SetLeaderboardCommand implements CommandExecutor, TabCompleter {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (sender instanceof Player player) {
                var location = player.getLocation();
                if (args.length < 1) {
                    player.sendMessage("Usage: /setleaderboard <mode>");
                    return true;
                }
                String modeName = args[0];
                if (modeName == null || modeName.isEmpty()) {
                    player.sendMessage("Usage: /setleaderboard <mode>");
                    return true;
                }
                Mode mode = TentoriumCore.modes.get(modeName);
                if (mode instanceof TrackedScore) {
                    location.setPitch(0);
                    location.setYaw(0);
                    TentoriumCore.leaderboard().put(mode, location);
                    player.sendMessage("Leaderboard location set for " + modeName);
                } else {
                    sender.sendMessage("Mode does not have trackable stats.");
                }
            } else {
                sender.sendMessage("You must be a player to run this command.");
            }
            return true;
        }
        @Override
        public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
            if (args.length <= 1) {
                List<String> suggestions = new ArrayList<>();
                TentoriumCore.modes.forEach((id, mode) -> {
                    if (!(mode instanceof TrackedScore)) return;
                    suggestions.add(id);
                });
                if (args.length == 1) {
                    return suggestions.stream().filter(string -> string.startsWith(args[0])).toList();
                }
                return suggestions;
            } else {
                return Collections.emptyList();
            }
        }
    }
} 