package com.what2see.model.user;

import com.what2see.model.tour.Report;
import com.what2see.model.tour.Review;
import com.what2see.model.tour.Tour;
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

    @OneToMany(mappedBy = "author")
    private List<Report> reportedTours;

    @OneToMany(mappedBy = "author")
    private List<Review> reviewedTours;

    @ManyToMany(mappedBy = "sharedTourists")
    private List<Tour> sharedTours;

    @ManyToMany(mappedBy = "markedTourists")
    private List<Tour> markedTours;

    @OneToMany(mappedBy = "tourist")
    private List<Conversation> conversations;

}
