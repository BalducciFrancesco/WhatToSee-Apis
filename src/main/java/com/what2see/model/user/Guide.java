package com.what2see.model.user;

import com.what2see.model.tour.Tour;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Guide extends User {

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Tour> createdTours;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL)
    private List<Conversation> conversations;

    @Override
    public String toString() {
        return "Guide{} " + super.toString();
    }
}
