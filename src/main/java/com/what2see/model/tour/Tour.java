package com.what2see.model.tour;

import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double approxCost;

    @Column(nullable = false)
    private String approxDuration;

    @CreationTimestamp
    @Column(nullable = false)
    private Date creationDate;

    @Column(nullable = false, columnDefinition = "tinyint(1) default 1")
    private boolean isPublic;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "guideId", nullable = false)
    private Guide author;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourStop> stops;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Report> reports;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "cityId", nullable = false)
    private City city;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
            name = "tour_tags",
            joinColumns = {@JoinColumn(name = "tourId")},
            inverseJoinColumns = {@JoinColumn(name = "tagId")}
    )
    private List<Tag> tags;

    @ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "themeId", nullable = false)
    private Theme theme;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
        name = "tour_shares",
        joinColumns = {@JoinColumn(name = "tourId")},
        inverseJoinColumns = {@JoinColumn(name = "touristId")}
    )
    private List<Tourist> sharedTourists;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinTable(
            name = "tour_completes",
            joinColumns = {@JoinColumn(name = "tourId")},
            inverseJoinColumns = {@JoinColumn(name = "touristId")}
    )
    private List<Tourist> markedTourists;


}
