package com.what2see.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Guide extends User {

    @OneToMany(mappedBy = "author")
    private List<Tour> createdTours;

    @OneToMany(mappedBy = "guide")
    private List<Conversation> conversations;
}
