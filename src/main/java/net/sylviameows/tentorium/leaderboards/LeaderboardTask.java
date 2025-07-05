package net.sylviameows.tentorium.leaderboards;

import net.sylviameows.tentorium.TentoriumCore;
import net.sylviameows.tentorium.config.Config;
import net.sylviameows.tentorium.database.Database;
import net.sylviameows.tentorium.database.SQLite;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Console;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LeaderboardTask extends BukkitRunnable {
    Calendar calendar = Calendar.getInstance();

    int seconds;

    public LeaderboardTask(TentoriumCore core) {
        this.runTaskTimer(core, 0L, 20L);
    }

    @Override
    public void run() {
        seconds++;

        if ((new Date().getTime() - getLast()) >= 3 /* days */ * 24 /* hours */ * 60 /* minutes */ * 60 /* seconds */ * 1000 /* milliseconds */) {
            var time = new Date();
            calendar.setTime(time);

//            TentoriumCore.logger().info(calendar.get(Calendar.DAY_OF_WEEK)+" "+calendar.get(Calendar.HOUR_OF_DAY));

            int day = calendar.get(Calendar.DAY_OF_WEEK);
            if (day == Calendar.THURSDAY) {
                var hour = calendar.get(Calendar.HOUR_OF_DAY);
                if (hour >= 22) {
                    setLast(time.getTime());
                    reset();
                }
            } else if (time.getTime() - getLast() >= 7 /* days */ * 24 /* hours */ * 60 /* minutes */ * 60 /* seconds */ * 1000 /* milliseconds */) {
                setLast(time.getTime());
                reset();
            }
        }

        if (seconds >= 30) {
            update();
            seconds = 0;
        }
    }

    private Long getLast() {
        return Config.get().getLong("leaderboard.last_reset", 0L);
    }
    private void setLast(Long last) {
        Config.get().set("leaderboard.last_reset", last);
        Config.save();
    }



    private void update() {
        TentoriumCore.leaderboard().updateAll();
    }

    private void reset() {
        TentoriumCore.logger().info("resetting leaderboard...");
        TentoriumCore.database().reset();
        TentoriumCore.logger().info("leaderboard database reset!");
    }
}
