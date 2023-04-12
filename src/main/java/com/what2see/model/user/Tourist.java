package com.what2see.model.user;

import com.what2see.model.tour.Report;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
public class Tourist extends User {

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Report> reportedTours;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Review> reviewedTours;

    @ManyToMany(mappedBy = "sharedTourists", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<Tour> sharedTours;

    @ManyToMany(mappedBy = "markedTourists", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    private List<Tour> markedTours;

    @OneToMany(mappedBy = "tourist", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

}
