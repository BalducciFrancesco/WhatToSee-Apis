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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    private final CityService cityService;

    private final TagService tagService;

    private final ThemeService themeService;

    private final UserService<Administrator> administratorService;

    private final UserService<User> userService;

    public Tour create(Tour t) {
        return tourRepository.save(t);
    }

    public void update(Tour oldTour, Tour newTour) {
        // doesn't update creationDate, reviews, reports and sharedTourists
        oldTour.setTitle(newTour.getTitle());
        oldTour.setDescription(newTour.getDescription());
        oldTour.setPublic(newTour.isPublic());
        oldTour.setCity(newTour.getCity());
        oldTour.setTags(newTour.getTags());
        oldTour.setTheme(newTour.getTheme());
        oldTour.setApproxCost(newTour.getApproxCost());
        oldTour.setApproxDuration(newTour.getApproxDuration());
        newTour.getStops().forEach(s -> s.setTour(oldTour));
        oldTour.setStops(newTour.getStops());
        oldTour.setSharedTourists(newTour.getSharedTourists());
    }

    public List<Tour> search(User u, TourSearchDTO s) throws NoSuchElementException {
        // if search field ids are specified, they must match an existing entity
        City cityFilter = s.getCityId() != null ? cityService.findById(s.getCityId()) : null;
        Theme themeFilter = s.getThemeId() != null ? themeService.findById(s.getThemeId()) : null;
        List<Tag> tagsFilter = s.getTagIds() != null ? tagService.findAllById(s.getTagIds()) : null;

        TourSearchStrategy strategy = switch(UserRoleMapper.mapUserToRole(u)) {
            case TOURIST -> new TourSearchStrategyTourist();
            case GUIDE -> new TourSearchStrategyGuide();
            case ADMINISTRATOR -> new TourSearchStrategyAdministrator();
        };
        TourSearch searcher = new TourSearch(strategy, tourRepository);
        return searcher.searchWithStrategy(u, cityFilter, themeFilter, s.getApproxDuration(), tagsFilter);
    }

    public Tour findById(Long tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow();
    }

    public List<Tour> findAllByReported(boolean isReported) {
        return tourRepository.findAllByReported(isReported);
    }

    // if is public or is author or is one of the shared tourists
    public boolean isVisible(Tour t, Long userId) {
        try {
            return t.isPublic() || (t.getAuthor().getId().equals(userId) || t.getSharedTourists().stream().anyMatch(tt -> tt.getId().equals(userId))) || administratorService.findById(userId) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEditable(Tour t, Long userId) {
        return t.getAuthor().getId().equals(userId);
    }

    public boolean isDeletable(Tour t, Long userId) {
        try {
            return t.getAuthor().getId().equals(userId) || administratorService.findById(userId) != null;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void markAsCompleted(Tour t, Tourist tt) {
        List<Tourist> completes = t.getMarkedTourists();
        if(completes.contains(tt))
            throw new InteractionAlreadyPerformedException(t, tt);
        completes.add(tt);
    }

    public void delete(Tour t) {
        tourRepository.delete(t);
    }

    public TourActionsResponseDTO getAvailableActions(Tour t, Long userId) {
        UserRole role = UserRoleMapper.mapUserToRole(userService.findById(userId));
        return TourActionsResponseDTO.builder()
                .createReport(role == UserRole.TOURIST && t.getReports().stream().noneMatch(r -> r.getAuthor().getId().equals(userId)))
                .markAsCompleted(role == UserRole.TOURIST && t.getMarkedTourists().stream().noneMatch(tt -> tt.getId().equals(userId)))
                .review(role == UserRole.TOURIST && t.getReviews().stream().noneMatch(r -> r.getAuthor().getId().equals(userId)))
                .sendMessage(role == UserRole.TOURIST)
                .edit(role == UserRole.GUIDE && t.getAuthor().getId().equals(userId))
                .viewReports(role == UserRole.ADMINISTRATOR)
                .delete(role == UserRole.ADMINISTRATOR || (role == UserRole.GUIDE && t.getAuthor().getId().equals(userId)))
                .build();
    }

    public List<Tour> getCompletedTours(Tourist tt) {   // excludes no longer visible
        return tt.getMarkedTours().stream().filter(t -> t.isPublic() || t.getSharedTourists().contains(tt)).collect(Collectors.toList());
    }
}
