package com.what2see.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

@Data
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    @Range(min = 0, max = 5)
    private int stars;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @ManyToOne
    @JoinColumn(name = "touristId", nullable = false)
    private Tourist author;
}
