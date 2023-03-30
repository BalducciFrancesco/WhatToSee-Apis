package com.what2see.model.user;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "touristId", nullable = false)
    private Tourist tourist;

    @ManyToOne
    @JoinColumn(name = "guideId", nullable = false)
    private Guide guide;

    @OneToMany(mappedBy = "conversation")
    private List<Message> messageList;

}
