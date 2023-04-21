package com.escapedoom.gamesession.data;

import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private EscapeRoomState state;
}
