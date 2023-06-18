package com.what2see;

import com.what2see.model.tour.*;
import com.what2see.model.user.*;
import com.what2see.repository.tour.*;
import com.what2see.repository.user.ConversationRepository;
import com.what2see.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mock utility class for all entities.<br>
 * Provides methods to get all or a specific entity for all available types.<br>
 * Note that the returned entities are not mocked, but retrieved from the actual repository, that is assumed
 * to be working and populated with testable data (see <code>import.sql</code> in <code>src/main/resources</code>).<br>
 */
@Component
@RequiredArgsConstructor
public class EntityMock {

    // -----------
    // TOUR
    // -----------

    // dependencies autowired by spring boot

    private final CityRepository cityRepository;

    private final TagRepository tagRepository;

    private final ThemeRepository themeRepository;

    private final ReportRepository reportRepository;

    private final TourRepository tourRepository;


    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCity() {
        return cityRepository.findById(1L).orElseThrow();
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTag() {
        return tagRepository.findById(1L).orElseThrow();
    }

    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }

    public Theme getTheme() {
        return themeRepository.findById(1L).orElseThrow();
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour getTour() {
        Tour t = tourRepository.findById(1L).orElseThrow();
        // force loading of all lazy-loaded collections
        t.getStops().size();
        t.getSharedTourists().size();
        t.getReviews().size();
        return t;
    }

    public Report getReport() {
        return reportRepository.findById(1L).orElseThrow();
    }

    // -----------
    // USER
    // -----------

    // dependencies autowired by spring boot

    private final UserRepository<User> userRepository;

    private final UserRepository<Tourist> touristRepository;

    private final UserRepository<Guide> guideRepository;

    private final UserRepository<Administrator> administratorRepository;

    private final ConversationRepository conversationRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Tourist> getAllTourists() {
        return touristRepository.findAll();
    }

    public Tourist getTourist() {
        return touristRepository.findById(1L).orElseThrow();
    }

    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    public Guide getGuide() {
        return guideRepository.findById(3L).orElseThrow();
    }

    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    public Administrator getAdministrator() {
        return administratorRepository.findById(5L).orElseThrow();
    }

    public List<Conversation> getAllConversations() {
        return conversationRepository.findAll();
    }

    public Conversation getConversation() {
        return conversationRepository.findById(1L).orElseThrow();
    }

}
