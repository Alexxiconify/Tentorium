package net.sylviameows.tentorium;

import net.sylviameows.tentorium.modes.Mode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TentoriumPlayer {
    private final Player parent;

    private Mode mode = null;

    public TentoriumPlayer(Player parent) {
        this.parent = parent;
    }

    public Mode mode() {
        return mode;
    }
    public void mode(Mode mode) {
        this.mode = mode;
    }

    public Player parent() {
        return parent;
    }

    /* FFA */
    private final FFASettings ffa = new FFASettings();
    public FFASettings ffa() {
        return ffa;
    }
    public static class FFASettings {
        private String kit = "shuffle";

        public String kit() {
            return kit;
        }
        public void kit(String mode) {
            this.kit = mode;
        }
    }

    /* Parkour */
    private final ParkourSettings parkour = new ParkourSettings();
    public ParkourSettings parkour() {
        return parkour;
    }
    public static class ParkourSettings {
        private String map = null;
        private Location checkpoint = null;

        public String map() {
            return map;
        }
        public void map(String map) {
            this.map = map;
        }

        public Location checkpoint() {
            return checkpoint;
        }
        public void checkpoint(Location checkpoint) {
            this.checkpoint = checkpoint;
        }

        public void reset() {
            this.map = null;
            this.checkpoint = null;
        }
    }
}
