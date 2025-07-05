package net.sylviameows.tentorium.database;

import net.sylviameows.tentorium.TentoriumCore;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public class LeaderboardResponse {
    private Player target = null;
    private int target_place = -1;

    private LeaderboardPlayer[] top_5;

    /**
     * Does nothing special at the moment.
     */
    public LeaderboardResponse(String stat, Player player) {
        // fixme: target = player;
        execute(stat);
    }

    public LeaderboardResponse(String stat) {
        execute(stat);
    }


    private void execute(String stat) {
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

        this.top_5 = top_5;

        // fixme: this statement doesnt work to get the targets place.
        if (target != null) {
            db.executeQuery("SELECT * FROM {{table}} ORDER BY "+stat+" DESC WHERE uuid = '"+ target.getUniqueId() +"';", result -> {
                try {
                    target_place = result.getRow();
                } catch (SQLException e) {
                    target_place = -1;
                }
            });
        }
    }

    public void list(BiConsumer<Integer, LeaderboardPlayer> consumer) {
        int place = 1;
        for (LeaderboardPlayer player : top_5) {
            consumer.accept(place, player);
            place++;
        }
    }

    public void forEach(BiConsumer<Integer, LeaderboardPlayer> consumer) {
        int place = 1;
        for (LeaderboardPlayer player : top_5) {
            consumer.accept(place, player);
            place++;
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
