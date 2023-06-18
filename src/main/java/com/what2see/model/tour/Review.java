package com.what2see.model.tour;

import com.what2see.model.user.Tourist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.util.Date;

/**
 * Entity that represents a review in the database.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    @Range(min = 1, max = 5)
    private int stars;

    @Column(nullable = false)
    private String description;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "tourId", nullable = false)
    private Tour tour;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "touristId", nullable = false)
    private Tourist author;

}
