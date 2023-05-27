package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.EscapeRoomDao;
import com.escapedoom.gamesession.data.Player;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TestRepo extends JpaRepository<EscapeRoomDao, Long> {

    @Query(
            value = "SELECT es.stage FROM EscapeRoomDao es where es.roomId = ?1 and es.stageId = ?2"
    )
    ArrayList<Object> getEscapeRoomStageByEscaperoomIDAndStageNumber(@Param("escapeRoomId") Long escapeRoomId, @Param("stageId") Long StageId);

}
