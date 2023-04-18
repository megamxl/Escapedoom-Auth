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
public class Escaperoom {
    @Id
    @GeneratedValue
    private Long escaperoom_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "user_id")
    private User user;
}
