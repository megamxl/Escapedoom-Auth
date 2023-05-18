package com.escapedoom.auth.data.dataclasses.models.escaperoom;

import com.escapedoom.auth.data.dataclasses.models.escaperoom.nodes.EscapeRoomStage;
import com.escapedoom.auth.data.dataclasses.models.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String topic;

    private long time;

    @OneToMany(mappedBy = "escaperoom", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<EscapeRoomStage> escapeRoomStages;
    @Override
    public String toString() {
        return "Escaperoom{" +
                "escaperoom_id=" + escaperoom_id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", time=" + time +
                '}';
    }
}
