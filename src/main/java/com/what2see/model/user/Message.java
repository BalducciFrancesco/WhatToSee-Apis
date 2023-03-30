package com.what2see.model.user;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String content;

    // true if from guide to user
    @Column(nullable = false)
    private boolean direction;


    @CreationTimestamp
    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne
    @JoinColumn(name = "conversationId", nullable = false)
    private Conversation conversation;
}
