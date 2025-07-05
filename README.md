# Tentorium

A fun minigames plugin for the Tentorium server, featuring various game modes including FFA, Spleef, TNT Run, Knockback, and Parkour.

## Features

- **Free For All (FFA)**: Multiple kits (Knight, Royal, Archer) with customizable loadouts
- **Spleef**: Classic snow-based spleef with WorldEdit integration
- **TNT Run**: Rainbow-themed TNT run with falling blocks
- **Knockback**: Simple knockback battles with sticks
- **Parkour**: Multi-difficulty parkour maps with checkpoints
- **Leaderboards**: Track scores and display them in-game
- **GUI System**: Modern GUI for mode and kit selection

## Building

This project uses Maven for building. Make sure you have Java 21+ installed.

### Using Maven Wrapper (Recommended)

```bash
# Build the plugin
./mvnw clean package

# The plugin JAR will be in target/tentorium-1.0.0.jar
```

### Using Maven directly

```bash
# Build the plugin
mvn clean package

# The plugin JAR will be in target/tentorium-1.0.0.jar
```

## Installation

1. Build the plugin using the commands above
2. Place the generated JAR file in your Paper server's `plugins/` folder
3. Start/restart your server
4. The plugin will automatically create its configuration files

## Dependencies

- **Paper 1.20.4+**: The server platform
- **WorldEdit**: For Spleef map regeneration
- **FastAsyncWorldEdit**: For better performance with WorldEdit operations
- **SQLite**: For player statistics and leaderboards

## Commands

- `/join <mode>` - Join a specific game mode
- `/leave` - Leave current game mode and return to spawn
- `/modes` - Open the mode selection GUI
- `/kit [kit_name]` - Select a kit for FFA mode
- `/select <map>` - Select a parkour map
- `/stats` - View your game statistics
- `/leaderboard <mode>` - View leaderboards for each game
- `/bypass` - Toggle spawn protection bypass (admin)
- `/setleaderboard <mode>` - Set leaderboard location for a mode (admin)

## Permissions

- `tentorium.*` - Access to all Tentorium commands
- `tentorium.bypass` - Allows bypassing spawn protections
- `tentorium.set_leaderboard` - Allows setting leaderboard locations

## Configuration

The plugin creates a `config.yml` file with settings for:
- Spawn location
- Game mode areas and void levels
- Spleef floor configurations
- TNT Run layer settings

## License

This project is licensed under the GNU Affero General Public License v3.0 - see the [LICENSE](LICENSE) file for details.

## Development

This plugin was created as a fun side project for the Tentorium minigames server. It's built using modern Java 21 features and follows Paper plugin best practices.