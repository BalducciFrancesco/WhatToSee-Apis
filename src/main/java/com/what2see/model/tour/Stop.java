package com.what2see.model.tour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity that represents a tour stop in the database.<br>
 * Usually is used as a part of a tour.
 * @see Tour
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private String duration;

    @Column(nullable = false)
    private Double transferCost;

    @Column(nullable = false)
    private String transferDuration;

    @Column(nullable = false)
    private String transferType;

    @Column(nullable = false)
    private String transferDetails;

    private String transferOtherOptions;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

}
