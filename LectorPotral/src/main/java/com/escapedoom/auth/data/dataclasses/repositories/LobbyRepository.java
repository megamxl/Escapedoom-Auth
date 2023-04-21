package com.escapedoom.auth.data.dataclasses.repositories;


import com.escapedoom.auth.data.dataclasses.models.escaperoom.Escaperoom;
import com.escapedoom.auth.data.dataclasses.models.escaperoom.OpenLobbys;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LobbyRepository extends JpaRepository<OpenLobbys, Long> {

    //Optional<List<Escaperoom>> findEscaperoomByUser(User User);

    void deleteByEscaperoomAndUser(Escaperoom escaperoom, User user);

    Optional<OpenLobbys> findByEscaperoomAndUser(Escaperoom escaperoom, User user);

    @Query(value = "select * from open_lobbys op where op.state not like 'STOPED'", nativeQuery = true)
    Optional<OpenLobbys> findByEscaperoomAndUserAndStateStopedNot(Escaperoom escaperoom, User user);

}
