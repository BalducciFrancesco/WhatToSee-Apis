package com.what2see.service.tour;

import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.model.tour.City;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Theme;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Tourist;
import com.what2see.repository.tour.TourRepository;
import com.what2see.utils.TourSearchResultComparator;
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


    public Tour create(Tour t) {
        return tourRepository.save(t);
    }

    public void update(Tour oldTour, Tour newTour) {
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

    public List<Tour> search(TourSearchDTO s) throws NoSuchElementException {
        // if search field ids are specified, they must match an existing entity
        City city = s.getCityId() != null ? cityService.findById(s.getCityId()) : null;
        Theme theme = s.getThemeId() != null ? themeService.findById(s.getThemeId()) : null;
        List<Tag> tags = s.getTagIds() != null ? tagService.findAllById(s.getTagIds()) : null;

        List<Tour> result = tourRepository.search(city, theme, s.getApproxDuration());
        if(tags != null && !tags.isEmpty()) {
            // filter the tags for those who contain at least one of the requested filters
            result = result.stream().filter(tour -> tour.getTags().stream().anyMatch(tags::contains)).collect(Collectors.toList());
        }

        result.sort(new TourSearchResultComparator());  // sort primarily by reviews count and secondarily for marked count
        return result;
    }

    public Tour findById(Long tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow();
    }

    // if is public or is author or is one of the shared tourists
    public boolean checkVisibility(Tour t, Long userId) {
        return t.isPublic() || (t.getAuthor().getId().equals(userId) || t.getSharedTourists().stream().anyMatch(tt -> tt.getId().equals(userId)));
    }

    public void markAsCompleted(Tour t, Tourist tt) {
        List<Tourist> completes = t.getMarkedTourists();
        if(!completes.contains(tt)) {
            completes.add(tt);
        }
    }

    public void delete(Tour t) {
        tourRepository.delete(t);
    }

}
