package com.escapedoom.auth.data.dataclasses.models.escaperoom;

import com.escapedoom.auth.data.dataclasses.models.user.User;
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
    private Long lobby_Id;

    @OneToOne
    private Escaperoom escaperoom;

    @OneToOne
    private User user;

}
