package com.escapedoom.gamesession.services;

import com.escapedoom.gamesession.data.escapeRoomDtos.LeaderboardDao;
import com.escapedoom.gamesession.repositories.SessionManagementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaderboardService {
    //TODO SCHMOMI:) this looks like nice class

    private final SessionManagementRepository repository;


    public List<LeaderboardDao> getScoreBoard() {
        //TODO SCHMOMI:) hier muss man was anderes machen
        return Collections.emptyList();
    }


}
