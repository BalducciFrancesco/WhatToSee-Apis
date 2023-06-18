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

/**
 * Repository that provides CRUD operations for {@link Tour} entities.
 */
@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    /*
     * All search methods with city, theme and approxDuration filters allow them to be null, in which case they are considered.
     * Note that here it's not filtering for tags, since it will be done service-side because of framework limitations.
     */

    /**
     * Searches for tours that are public or shared with the given tourist.
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param sharedWith tourist to check if tour is shared with (NOT optional)
     * @return list of tours that match the given criteria
     */
    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt LEFT JOIN t.sharedTourists ttt WHERE " +
            "(t.isPublic = true OR ttt = :sharedWith) AND " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchPublicOrSharedWith(City city, Theme theme, String approxDuration, Tourist sharedWith);

    /**
     * Searches for tours that are public or created by the given guide.
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @param author guide to check if tour is created by (NOT optional)
     * @return list of tours that match the given criteria
     */
    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt WHERE " +
            "(t.isPublic = TRUE OR t.author = :author) AND " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchPublicOrCreatedBy(City city, Theme theme, String approxDuration, Guide author);

    /**
     * Searches for tours without considering their visibility status.<br>
     * It is intended to only be used by administrators.
     * @param city city filter (optional)
     * @param theme theme filter (optional)
     * @param approxDuration maximum duration filter (optional)
     * @return list of tours that match the given criteria
     */
    @Query("SELECT t FROM Tour t LEFT JOIN t.tags tt WHERE " +
            "(:city IS NULL OR t.city = :city) AND " +
            "(:theme IS NULL OR t.theme = :theme) AND " +
            "(:approxDuration IS NULL OR t.approxDuration <= :approxDuration)")
    List<Tour> searchAll(City city, Theme theme, String approxDuration);

    /**
     * Searched for tours that have at least one report or none.
     * @param isReported whether to search for tours with reports or without
     * @return list of tours that match the given criteria
     */
    @Query("SELECT t from Tour t LEFT JOIN t.reports r WHERE " +
            "(:isReported = TRUE AND r.id <> NULL) OR " +
            "(:isReported = FALSE AND r.id = NULL)")
    List<Tour> findAllByReported(boolean isReported);

}
