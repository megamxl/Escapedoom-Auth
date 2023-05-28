package com.escapedoom.gamesession.data.escapeRoomDtos;

import com.escapedoom.gamesession.data.Player;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

@Data
public class LeaderboardDao {

    //TODO SCHMOMI:) this class looks promising

    private String PlayerName;

    private Long score;

    //TODO: Add Timestamp for last solve

    public LeaderboardDao(Player player) {
        PlayerName = player.getName();
        this.score = player.getScore();
    }
}
