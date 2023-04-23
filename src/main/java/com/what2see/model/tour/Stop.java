package com.what2see.model.tour;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", duration='" + duration + '\'' +
                ", transferCost=" + transferCost +
                ", transferDuration='" + transferDuration + '\'' +
                ", transferType='" + transferType + '\'' +
                ", transferDetails='" + transferDetails + '\'' +
                ", transferOtherOptions='" + transferOtherOptions + '\'' +
                '}';
    }
}
