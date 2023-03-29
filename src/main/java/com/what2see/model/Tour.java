package com.what2see.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Data
@Entity
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private Double approxCost;

    @Column(nullable = false)
    private String approxDuration;

    @CreationTimestamp
    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isPublic;

    @ManyToOne
    @JoinColumn(name = "guideId", nullable = false)
    private Guide author;

    @OneToMany(mappedBy = "tour")
    private List<TourStop> stops;

    @OneToMany(mappedBy = "tour")
    private List<Report> reports;

    @OneToMany(mappedBy = "tour")
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name = "cityId")
    private City city;

    @ManyToMany
    @JoinTable(
            name = "tour_tags",
            joinColumns = {@JoinColumn(name = "tourId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")}
    )
    private List<Tag> tags;

    @ManyToOne
    @JoinColumn(name = "themeId", nullable = false)
    private Theme theme;

    @ManyToMany
    @JoinTable(
        name = "tour_shares",
        joinColumns = {@JoinColumn(name = "tourId")},
        inverseJoinColumns = {@JoinColumn(name = "touristId")}
    )
    private List<Tourist> sharedTourists;

    @ManyToMany
    @JoinTable(
            name = "tour_completes",
            joinColumns = {@JoinColumn(name = "tourId")},
            inverseJoinColumns = {@JoinColumn(name = "touristId")}
    )
    private List<Tourist> markedTourists;
}
