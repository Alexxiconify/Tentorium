package net.sylviameows.tentorium.database;

import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class LeaderboardResponse {
    private final LeaderboardPlayer[] top_5;

    public LeaderboardResponse(String stat, Player player) {
        // TODO: Implement target player tracking
        this.top_5 = execute(stat);
    }

    public LeaderboardResponse(String stat) {
        this.top_5 = execute(stat);
    }

    private LeaderboardPlayer[] execute(String stat) {
        var db = TentoriumCore.database();
        LeaderboardPlayer[] top_5 = new LeaderboardPlayer[5];
        AtomicInteger place = new AtomicInteger(0);

        db.executeQuery("SELECT * FROM {{table}} ORDER BY "+stat+" DESC LIMIT 5;", result -> {
            try {
                var name = result.getString("name");
                var score = result.getInt(stat);
                top_5[place.getAndIncrement()] = new LeaderboardPlayer(name, score);
            } catch (SQLException e) {
                top_5[place.getAndIncrement()] = null;
            }
        });

        return top_5;
    }

    public void forEach(BiConsumer<Integer, LeaderboardPlayer> consumer) {
        for (int i = 0; i < top_5.length; i++) {
            consumer.accept(i + 1, top_5[i]);
        }
    }

    public record LeaderboardPlayer(String name, int score) {
        public String name() {
            return name;
        }

        public int score() {
            return score;
        }
    }
}
