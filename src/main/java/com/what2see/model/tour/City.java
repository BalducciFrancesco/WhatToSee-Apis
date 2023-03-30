package com.what2see.model.tour;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<Tour> tours;
}
