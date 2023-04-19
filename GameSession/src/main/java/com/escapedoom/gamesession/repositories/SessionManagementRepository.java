package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionManagementRepository extends JpaRepository<Player, Long> {

    Player findPlayerBySessionID(String sessionID);
}
