package net.sylviameows.tentorium.modes;

import net.sylviameows.tentorium.database.LeaderboardResponse;
import org.bukkit.entity.Player;

public interface TrackedScore {
    String leaderboardStatId();
    String leaderboardStatName();

    default LeaderboardResponse getLeaderboard() {
        return new LeaderboardResponse(leaderboardStatId());
    }

    default LeaderboardResponse getLeaderboard(Player player) {
        return new LeaderboardResponse(leaderboardStatId(), player);
    }

}
