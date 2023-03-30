package com.what2see.model.tour;

import com.what2see.model.user.Tourist;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "touristId", nullable = false)
    private Tourist author;

}
