package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.Player;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TestRepo extends JpaRepository<Player, Long> {

    @Query(
            value = "SELECT stage FROM escape_room_stage where escape_roomid = ?1 and stage_id = ?2",
            nativeQuery = true
    )
    ArrayList<Object> getEscapeRoomStageByEscaperoomIDAndStageNumber(@Param("escapeRoomId") Long escapeRoomId, @Param("stageId") Long StageId);

}
