package com.escapedoom.gamesession.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OpenLobbys {

    @Id
    @GeneratedValue
    private Long lobbyId;

    private Long escaperoom_escaperoom_id;

    private Long user_user_id;


}
