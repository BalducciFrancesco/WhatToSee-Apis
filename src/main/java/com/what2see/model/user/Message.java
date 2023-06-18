package com.what2see.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

/**
 * Entity that represents a message in the database.<br>
 * Usually is used as a part of a conversation.
 * @see Conversation
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Boolean direction;  // true if from guide to user


    @CreationTimestamp
    @Column(nullable = false)
    private Date timestamp;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "conversationId", nullable = false)
    private Conversation conversation;

}
