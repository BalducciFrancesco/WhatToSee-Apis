package com.what2see.service.tour;

import com.what2see.dto.tour.TourActionsResponseDTO;
import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.dto.user.UserRole;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.mapper.user.UserRoleMapper;
import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import com.what2see.repository.tour.TourRepository;
import com.what2see.service.tour.search.*;
import com.what2see.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service class that handles the business logic for {@link Tour} entities.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class TourService {

    // dependencies autowired by spring boot

    private final TourRepository tourRepository;

    private final CityService cityService;

    private final TagService tagService;

    private final ThemeService themeService;

    private final UserService<Administrator> administratorService;

    private final UserService<User> userService;

    /**
     * Creates a new {@link Tour} entity.
     * @param t tour to be created (without id)
     * @return created tour (with id)
     * @throws DataIntegrityViolationException if the tour is not valid (e.g. title already existing, etc.)
     */
    public Tour create(Tour t) throws DataIntegrityViolationException {
        return tourRepository.save(t);
    }

    /**
     * Updates an existing {@link Tour} entity.<br>
     * The tour to be updated must have an id.<br>
     * The following fields modifications are <b>ignored and not updated</b>:
     * <ul><li>
     *     id
     * </li><li>
     *     author
     * </li><li>
     *     creationDate
     * </li><li>
     *     reviews
     * </li><li>
     *     reports
     * </li></ul>
     * @param oldTour tour to be updated (from repository, with id)
     * @param newTour tour to be updated to (from client, without id)
     */
    public void update(Tour oldTour, Tour newTour) {
        oldTour.setTitle(newTour.getTitle());
        oldTour.setDescription(newTour.getDescription());
        oldTour.setPublic(newTour.isPublic());
        oldTour.setCity(newTour.getCity());
        oldTour.setTags(newTour.getTags());
        oldTour.setTheme(newTour.getTheme());
        oldTour.setApproxCost(newTour.getApproxCost());
        oldTour.setApproxDuration(newTour.getApproxDuration());
        newTour.getStops().forEach(s -> s.setTour(oldTour));    // important because of single-side relation ownership
        oldTour.setStops(newTour.getStops());
        oldTour.setSharedTourists(newTour.getSharedTourists());
    }

    /**
     * Searches for tours according to the specified search criteria (all optional) and based on the requesting {@link User}.<br>
     * In particular, the user role is used to determine the most appropriate search strategy to be used.<br>
     * @param u requesting user
     * @param s search criteria
     * @return list of sorted tours matching the search criteria
     * @throws NoSuchElementException if at least one of the search criteria is specified but does not match an existing entity
     * @see TourSearchStrategy
     * @see TourSearchDTO
     */
    public List<Tour> search(User u, TourSearchDTO s) throws NoSuchElementException {
        // find entities of search criteria from ids
        City cityFilter = s.getCityId() != null ? cityService.findById(s.getCityId()) : null;
        Theme themeFilter = s.getThemeId() != null ? themeService.findById(s.getThemeId()) : null;
        List<Tag> tagsFilter = s.getTagIds() != null ? tagService.findAllById(s.getTagIds()) : null;

        // find appropriate search strategy based on user role
        TourSearchStrategy strategy = switch(UserRoleMapper.mapUserToRole(u)) {
            case TOURIST -> new TourSearchStrategyTourist();
            case GUIDE -> new TourSearchStrategyGuide();
            case ADMINISTRATOR -> new TourSearchStrategyAdministrator();
        };
        // search and sort tours with the selected strategy, then return them
        TourSearch searcher = new TourSearch(strategy, tourRepository);
        return searcher.searchWithStrategy(u, cityFilter, themeFilter, s.getApproxDuration(), tagsFilter);
    }

    /**
     * Returns a {@link Tour} entity with the given id.
     * @param tourId id of the city
     * @return tour with the given id
     * @throws NoSuchElementException if no tour with the given id exists
     */
    public Tour findById(Long tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow();
    }

    /**
     * Returns all{@link Tour} entities that have at least one report or none.
     * @param isReported whether to search for tours with reports or without
     * @return list of tours that match the given criteria
     */
    public List<Tour> findAllByReported(boolean isReported) {
        return tourRepository.findAllByReported(isReported);
    }


    // if is public or is author or is one of the shared tourists

    /**
     * Returns whether the given {@link Tour} entity is visible to the given {@link User}.<br>
     * A tour is visible if it is:
     * <ul><li>
     *     public
     * </li><li>
     *     private but the requesting user is a guide and also its author
     * </li><li>
     *     private but the requesting user a tourist and also among the shared tourists
     * </li><li>
     *     private but the requesting user is an administrator
     * </li></ul>
     * @param t tour to be checked
     * @param userId requesting user id
     * @return whether the tour is visible to the user
     */
    public boolean isVisible(Tour t, Long userId) {
        try {
            return t.isPublic() || (t.getAuthor().getId().equals(userId) || t.getSharedTourists().stream().anyMatch(tt -> tt.getId().equals(userId))) || administratorService.findById(userId) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns whether the given {@link Tour} entity is deletable by the given {@link User}.<br>
     * A tour is deletable if the requesting user is:
     * <ul><li>
     *     a guide and also its author
     * </li><li>
     *     an administrator
     * </li></ul>
     * @param t tour to be checked
     * @param userId requesting user id
     * @return whether the tour is deletable by the user
     */
    public boolean isDeletable(Tour t, Long userId) {
        try {
            return t.getAuthor().getId().equals(userId) || administratorService.findById(userId) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Deletes the given {@link Tour} entity.<br>
     * In case this action is requested by client side, it is recommended to first {@link #isDeletable(Tour, Long) check} whether the action is allowed.
     * @param t tour to be deleted
     */
    public void delete(Tour t) {
        tourRepository.delete(t);
    }

    /**
     * Marks the given {@link Tour} entity as completed by the given {@link Tourist} entity.<br>
     * @param t tour to be marked as completed
     * @param tt requesting tourist
     * @throws InteractionAlreadyPerformedException if the tourist has already marked the tour as completed
     */
    public void markAsCompleted(Tour t, Tourist tt) throws InteractionAlreadyPerformedException {
        // check if the tourist has already marked the tour as completed
        List<Tourist> completes = t.getMarkedTourists();
        if(completes.contains(tt))
            throw new InteractionAlreadyPerformedException(t, tt);

        completes.add(tt);
    }

    /**
     * Returns a {@link TourActionsResponseDTO} containing the available actions for the given {@link User} onto the specified {@link Tour} entity<br>
     * Its purpose is to provide the client a way to dynamically show or hide UI elements based on actually performable actions.<br>
     * It should not replace server side checks.
     * @param t tour to be checked
     * @param userId requesting user id
     * @return available actions for the user
     * @see TourActionsResponseDTO
     */
    public TourActionsResponseDTO getAvailableActions(Tour t, Long userId) {
        UserRole role = UserRoleMapper.mapUserToRole(userService.findById(userId)); // get user role
        return TourActionsResponseDTO.builder()
                // create report: if user is tourist and has not already performed the action
                .createReport(role == UserRole.TOURIST && t.getReports().stream().noneMatch(r -> r.getAuthor().getId().equals(userId)))
                // mark as completed: see above
                .markAsCompleted(role == UserRole.TOURIST && t.getMarkedTourists().stream().noneMatch(tt -> tt.getId().equals(userId)))
                // review: see above, plus it must have previously marked the tour as completed
                .review(role == UserRole.TOURIST && t.getReviews().stream().noneMatch(r -> r.getAuthor().getId().equals(userId)) && t.getMarkedTourists().stream().map(User::getId).anyMatch(userId::equals))
                // send message to guide from tour page: if user is tourist
                .sendMessage(role == UserRole.TOURIST)
                // edit tour: if user is guide and is also the author
                .edit(role == UserRole.GUIDE && t.getAuthor().getId().equals(userId))
                // view reports: if user is administrator
                .viewReports(role == UserRole.ADMINISTRATOR)
                // delete tour: if user is administrator or is guide and is also the author
                .delete(role == UserRole.ADMINISTRATOR || (role == UserRole.GUIDE && t.getAuthor().getId().equals(userId)))
                .build();
    }

    /**
     * Returns a list of {@link Tour} entities that are marked as completed by the given {@link Tourist} entity.<br>
     * Note that excludes tours that are no longer visible to the tourist because they have been made private by the author and the tourist is not among the shared tourists.
     * @param tt tourist to be checked
     * @return list of tours that match the given criteria
     */
    public List<Tour> getCompletedTours(Tourist tt) {
        return tt.getMarkedTours().stream().filter(t -> t.isPublic() || t.getSharedTourists().contains(tt)).collect(Collectors.toList());
    }
}
