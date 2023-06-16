package com.what2see.repository.tour;

import com.what2see.model.tour.City;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // all fields are optional filters for search
    // also not searching for tags, will be done code-side

    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt LEFT JOIN t.sharedTourists ttt WHERE " +
            "(t.isPublic = true OR ttt = :sharedWith) AND " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchPublicOrSharedWith(City city, Theme theme, String approxDuration, Tourist sharedWith);

    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt WHERE " +
            "(t.isPublic = TRUE OR t.author = :author) AND " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchPublicOrCreatedBy(City city, Theme theme, String approxDuration, Guide author);

    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt WHERE " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchAll(City city, Theme theme, String approxDuration);

    @Query("SELECT t from Tour t LEFT JOIN t.reports r WHERE " +
            "(:isReported = TRUE AND r.id <> NULL) OR " +
            "(:isReported = FALSE AND r.id = NULL)")
    List<Tour> findAllByReported(boolean isReported);

}
