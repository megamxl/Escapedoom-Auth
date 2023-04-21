package com.escapedoom.auth.data.dataclasses.models.escaperoom;

import com.escapedoom.auth.data.dataclasses.models.user.User;
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
    private Long lobby_Id;

    @OneToOne
    private Escaperoom escaperoom;

    @OneToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private EscapeRoomState state;

}
