package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.dto.tour.TourActionsResponseDTO;
import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.model.tour.Stop;
import com.what2see.model.tour.Tag;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
import com.what2see.model.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link TourService}.
 */
@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TourServiceTest {

    // dependencies autowired by spring boot

    private final EntityMock mock;

    private final TourService tourService;

    /**
     * Using the entity manager to avoid updating the database after editing an entity's fields.
     * @see TourServiceTest#update()
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Tests {@link TourService#create(Tour)} in the successful case.<br>
     * Ensures that a new tour (with a new id and the same title and stops count) is created.
     */
    @Test
    void create() {
        // setup
        Stop stop = Stop.builder()
                .title("Stop1")
                .description("StopDescription1")
                .cost(5.0)
                .duration("05:00")
                .transferCost(1.0)
                .transferDuration("00:30")
                .transferType("TransferType1")
                .transferDetails("TransferDetails1")
                .transferOtherOptions("TransferOtherOptions1")
                .build();
        Tour expected = Tour.builder()
                .title("Tour1")
                .description("TourDescription1")
                .approxCost(15.0)
                .approxDuration("06:30")
                .isPublic(true)
                .author(mock.getGuide())
                .stops(List.of(stop))
                .city(mock.getCity())
                .theme(mock.getTheme())
                .build();
        stop.setTour(expected); // important because of single-side relation ownership
        // under test
        Tour underTest = tourService.create(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getTitle(), underTest.getTitle());
        assertEquals(1, underTest.getStops().size());
    }

    /**
     * Tests {@link TourService#update(Tour, Tour)} in the successful case.<br>
     * Ensures that the tour is updated with the new values (title, visibility, stops, shared tourists) while others didn't change.<br>
     * Note that the tour is detached from the entity manager to avoid the database to be updated upon setting the new values.
     * @see TourServiceTest#entityManager
     */
    @Test
    void update() {
        // setup
        Tour expected = mock.getTour();
        entityManager.detach(expected); // detach to avoid updating the database after setting the new values

        expected.setTitle("Tour2");
        expected.setPublic(false);

        Stop stop = Stop.builder()
                .title("Stop2")
                .description("StopDescription2")
                .cost(5.0)
                .duration("05:00")
                .transferCost(2.0)
                .transferDuration("00:30")
                .transferType("TransferType2")
                .transferDetails("TransferDetails2")
                .transferOtherOptions("TransferOtherOptions2")
                .build();
        List<Stop> newStops = new ArrayList<>(expected.getStops());
        newStops.add(stop);
        expected.setStops(newStops);

        List<Tourist> newSharedTourists = Stream.concat(
                expected.getSharedTourists().stream(),  // should be empty
                mock.getAllTourists().stream().filter(t -> !t.getSharedTours().contains(mock.getTour()))
        ).toList();
        expected.setSharedTourists(newSharedTourists);

        // under test
        tourService.update(mock.getTour(), expected);
        Tour underTest = mock.getTour();

        // assertion
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getTitle(), underTest.getTitle());

        assertEquals(3, underTest.getStops().size());
        assertEquals(expected.getStops().size(), underTest.getStops().size());

        assertEquals(1, underTest.getReviews().size());
        assertEquals(expected.getReviews().size(), underTest.getReviews().size());

        assertEquals(2, underTest.getSharedTourists().size());
        assertEquals(expected.getSharedTourists().size(), underTest.getSharedTourists().size());
    }

    /**
     * Tests {@link TourService#search(User, TourSearchDTO)} in the successful case with all filters and a single expected result.<br>
     * Ensures that the search returns the expected results for each role.
     * @see com.what2see.service.tour.search.TourSearchStrategy
     */
    @Test
    void searchSingleResult() {
        // setup
        Tour expected = mock.getTour();
        TourSearchDTO searchParams = TourSearchDTO.builder()    // very specific filters: should return only 1 result regardless of role
                .themeId(expected.getTheme().getId())
                .approxDuration(expected.getApproxDuration())
                .cityId(expected.getCity().getId())
                .tagIds(expected.getTags().stream().map(Tag::getId).toList())
                .build();
        // under test
        List<Tour> underTestTourist = tourService.search(mock.getTourist(), searchParams);
        List<Tour> underTestGuide = tourService.search(mock.getGuide(), searchParams);
        List<Tour> underTestAdministrator = tourService.search(mock.getAdministrator(), searchParams);
        // assertion
        assertEquals(1, underTestTourist.size());
        assertEquals(1, underTestGuide.size());
        assertEquals(1, underTestAdministrator.size());
    }

    /**
     * Tests {@link TourService#search(User, TourSearchDTO)} in the successful case with no filters and multiple expected results.<br>
     * Ensures that the search returns the expected results for the {@link com.what2see.service.tour.search.TourSearchStrategyTourist tourist strategy}.
     * @see com.what2see.service.tour.search.TourSearch
     * @see com.what2see.service.tour.search.TourSearchStrategyTourist
     */
    @Test
    void searchMultipleResultsTourist() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any further filtering except for role
        Tourist subject = mock.getTourist();

        // simulating the tourist search and sort strategy
        List<Long> expectedIds = Stream.concat(
                mock.getAllTours().stream().filter(Tour::isPublic), // public
                subject.getSharedTours().stream()   // shared with subject
        ).sorted((t1, t2) -> {  // descendant order primarily for reviews count and secondarily for marked count
            int reviewComp = t2.getReviews().size() - t1.getReviews().size();
            return reviewComp != 0 ? reviewComp : t2.getMarkedTourists().size() - t1.getMarkedTourists().size();
        }).map(Tour::getId).toList();
        // under test
        List<Long> underTestIds = tourService.search(subject, searchParams).stream().map(Tour::getId).toList();
        // assertion
        assertEquals(expectedIds.size(), underTestIds.size());
        assertEquals(expectedIds, underTestIds);    // same elements and same order
    }

    /**
     * Tests {@link TourService#search(User, TourSearchDTO)} in the successful case with no filters and multiple expected results.<br>
     * Ensures that the search returns the expected results for the {@link com.what2see.service.tour.search.TourSearchStrategyGuide guide strategy}.
     * @see com.what2see.service.tour.search.TourSearch
     * @see com.what2see.service.tour.search.TourSearchStrategyGuide
     */
    @Test
    void searchMultipleResultsGuide() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any further filtering except for role
        Guide subject = mock.getGuide();

        // simulating the guide search and sort strategy
        List<Long> expectedIds = mock.getAllTours().stream()
            .filter(t -> t.isPublic() || t.getAuthor().getId().equals(subject.getId())) // public or created by guide
            .sorted((t1, t2) -> { // descendant order primarily for reviews count and secondarily for marked count showing first those created by current guide
                if((t1.getAuthor().getId().equals(subject.getId()) || t2.getAuthor().getId().equals(subject.getId())) && !t1.getAuthor().getId().equals(t2.getAuthor().getId())) {
                    // only one of them has current guide as author, show that first
                    return t1.getAuthor().getId().equals(subject.getId()) ? -1 : 1;
                } else {
                    // both or none of them has current guide as author, sort like tourist
                    int reviewComp = t2.getReviews().size() - t1.getReviews().size();
                    return reviewComp != 0 ? reviewComp : t2.getMarkedTourists().size() - t1.getMarkedTourists().size();
                }
            }).map(Tour::getId).toList();
        // under test
        List<Long> underTestIds = tourService.search(subject, searchParams).stream().map(Tour::getId).toList();
        // assertion
        assertEquals(expectedIds.size(), underTestIds.size());
        assertEquals(expectedIds, underTestIds);    // same elements and same order
    }

    /**
     * Tests {@link TourService#search(User, TourSearchDTO)} in the successful case with no filters and multiple expected results.<br>
     * Ensures that the search returns the expected results for the {@link com.what2see.service.tour.search.TourSearchStrategyAdministrator administrator strategy}.
     * @see com.what2see.service.tour.search.TourSearch
     * @see com.what2see.service.tour.search.TourSearchStrategyAdministrator
     */
    @Test
    void searchMultipleResultsAdministrator() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any further filtering except for role
        Administrator subject = mock.getAdministrator();

        // simulating the administrator search and sort strategy
        List<Long> expectedIds = mock.getAllTours().stream()
            // no filter: public or private
            .sorted((t1, t2) -> { // descendant order primarily for reviews count and secondarily for marked count showing first those with most reports
                int reportComp = t2.getReports().size() - t1.getReports().size();
                int reviewComp = t2.getReviews().size() - t1.getReviews().size();
                int markedComp = t2.getMarkedTourists().size() - t1.getMarkedTourists().size();
                return reportComp != 0 ? reportComp : reviewComp != 0 ? reviewComp : markedComp;
            }).map(Tour::getId).toList();
        // under test
        List<Long> underTestIds = tourService.search(subject, searchParams).stream().map(Tour::getId).toList();
        // assertion
        assertEquals(expectedIds.size(), underTestIds.size());
        assertEquals(expectedIds, underTestIds);    // same elements and same order
    }

    /**
     * Tests {@link TourService#findById(Long)}.<br>
     * Ensures that the expected tour (by id and title) is returned.
     */
    @Test
    void findById() {
        // setup
        Tour expected = mock.getTour();
        // under test
        Tour underTest = tourService.findById(expected.getId());
        // assertion
        assertEquals(expected.getId(), underTest.getId());
        assertEquals(expected.getTitle(), underTest.getTitle());
    }

    /**
     * Tests {@link TourService#findAllByReported(boolean)}.<br>
     * Ensures that the expected tours (by id) are returned.
     */
    @Test
    void findAllByReported() {
        // setup
        List<Long> expectedIds1 = mock.getAllTours().stream().filter(t -> t.getReports().size() > 0).map(Tour::getId).toList();
        List<Long> expectedIds2 = mock.getAllTours().stream().filter(t -> t.getReports().size() == 0).map(Tour::getId).toList();
        // under test
        List<Tour> underTest1 = tourService.findAllByReported(true);
        List<Tour> underTest2 = tourService.findAllByReported(false);
        // assertion
        assertEquals(expectedIds1.size(), underTest1.size());
        assertEquals(expectedIds2.size(), underTest2.size());
        assertTrue(underTest1.stream().map(Tour::getId).allMatch(expectedIds1::contains));
        assertTrue(underTest2.stream().map(Tour::getId).allMatch(expectedIds2::contains));
    }

    /**
     * Tests {@link TourService#isVisible(Tour, Long)} in the successful case with a public tour and a tourist.<br>
     * Ensures that the tour is visible to the expected tourist.
     */
    @Test
    void isVisibleAsPublic() {
        // setup
        // find a tour that is public (otherwise the test would fail)
        Tour isPublic = mock.getAllTours().stream().filter(Tour::isPublic).findAny().orElseThrow();
        Tourist expectedVisible = mock.getTourist();
        // under test
        boolean underTestVisible = tourService.isVisible(isPublic, expectedVisible.getId());
        // assertion
        assertTrue(underTestVisible);
    }

    /**
     * Tests {@link TourService#isVisible(Tour, Long)} in the successful case with a private tour and an administrator.<br>
     * Ensures that the tour is visible to the expected administrator.
     */
    @Test
    void isVisibleAsAdministrator() {
        // setup
        // find a tour that is private (otherwise the test would fail)
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Administrator expectedVisible = mock.getAdministrator();
        // under test
        boolean underTestVisible = tourService.isVisible(isNotPublic, expectedVisible.getId());
        // assertion
        assertTrue(underTestVisible);
    }

    /**
     * Tests {@link TourService#isVisible(Tour, Long)} in the successful case with a private tour and a tourist that is among the shared ones.<br>
     * Ensures that the tour is visible to the expected tourist.
     */
    @Test
    void isVisibleAsShared() {
        // setup
        // find a tour that is private and one of its shared tourists (otherwise the test would fail)
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        List<Long> sharedTouristsIds = isNotPublic.getSharedTourists().stream().map(User::getId).toList();
        Tourist expectedVisible = mock.getAllTourists().stream().filter(t -> sharedTouristsIds.contains(t.getId())).findAny().orElseThrow();
        Tourist expectedNotVisible = mock.getAllTourists().stream().filter(t -> !sharedTouristsIds.contains(t.getId())).findAny().orElseThrow();
        // under test
        boolean underTestVisible = tourService.isVisible(isNotPublic, expectedVisible.getId());
        boolean underTestNotVisible = tourService.isVisible(isNotPublic, expectedNotVisible.getId());
        // assertion
        assertTrue(underTestVisible);
        assertFalse(underTestNotVisible);
    }

    /**
     * Tests {@link TourService#isVisible(Tour, Long)} in the successful case with a private tour and its author guide.<br>
     * Ensures that the tour is visible to the expected author.
     */
    @Test
    void isVisibleAsAuthor() {
        // setup
        // find a tour that is private and its author guide (otherwise the test would fail)
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Guide expectedVisible = isNotPublic.getAuthor();
        Guide expectedNotVisible = mock.getAllGuides().stream().filter(g -> !g.getId().equals(expectedVisible.getId())).findAny().orElseThrow();
        // under test
        boolean underTestVisible = tourService.isVisible(isNotPublic, expectedVisible.getId());
        boolean underTestNotVisible = tourService.isVisible(isNotPublic, expectedNotVisible.getId());
        // assertion
        assertTrue(underTestVisible);
        assertFalse(underTestNotVisible);
    }

    /**
     * Tests {@link TourService#isDeletable(Tour, Long)} in the successful and failing case.<br>
     * Ensures that the tour is deletable by its author guide but not from others.
     */
    @Test
    void isDeletableAsAuthor() {
        // setup
        Tour subject = mock.getTour();
        // find its author guide and another one (otherwise the test would fail)
        Guide expectedDeletable = subject.getAuthor();
        Guide expectedNotDeletable = mock.getAllGuides().stream().filter(g -> !g.getId().equals(expectedDeletable.getId())).findAny().orElseThrow();
        // under test
        boolean underTestDeletable = tourService.isDeletable(subject, expectedDeletable.getId());
        boolean underTestNotDeletable = tourService.isDeletable(subject, expectedNotDeletable.getId());
        // assertion
        assertTrue(underTestDeletable);
        assertFalse(underTestNotDeletable);
    }

    /**
     * Tests {@link TourService#isDeletable(Tour, Long)} in the successful case.<br>
     * Ensures that the tour is deletable by an administrator.
     */
    @Test
    void isDeletableAsAdministrator() {
        // setup
        Tour subject = mock.getTour();
        Administrator expectedDeletable = mock.getAdministrator();
        // under test
        boolean underTestDeletable = tourService.isDeletable(subject, expectedDeletable.getId());
        // assertion
        assertTrue(underTestDeletable);
    }

    /**
     * Tests {@link TourService#markAsCompleted(Tour, Tourist)} in the successful case.<br>
     * Ensures that the tour is marked as completed by the expected tourist.
     */
    @Test
    void markAsCompleted() {
        // setup
        Tourist subject = mock.getTourist();
        // find a tour that is not already marked by the subject (otherwise the test would fail)
        List<Long> expectedMarkedIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour expectedNotMarked = mock.getAllTours().stream().filter(t -> !expectedMarkedIds.contains(t.getId())).findAny().orElseThrow();
        // under test
        tourService.markAsCompleted(expectedNotMarked, subject);
        // assertion
        assertTrue(expectedNotMarked.getMarkedTourists().stream().map(User::getId).anyMatch(t -> t.equals(subject.getId())));
    }

    /**
     * Tests {@link TourService#markAsCompleted(Tour, Tourist)} in the failing case because the tourist has already marked the tour as completed.<br>
     * Ensures that a {@link InteractionAlreadyPerformedException} is thrown.
     */
    @Test
    void noMultipleMarkAsCompleted() {
        // setup
        // find a tour that is already marked and one of those tourists (otherwise the test would fail)
        Tour expectedMarked = mock.getAllTours().stream().filter(t ->  t.getMarkedTourists().size() > 0).findAny().orElseThrow();
        Tourist subject = expectedMarked.getMarkedTourists().stream().findAny().orElseThrow();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> tourService.markAsCompleted(expectedMarked, subject));
    }

    /**
     * Tests {@link TourService#delete(Tour)} in the successful case.<br>
     * Ensures that the tour is deleted.
     */
    @Test
    void delete() {
        // setup
        Tour expected = mock.getTour();
        // under test
        tourService.delete(expected);
        // assertion
        assertThrows(NoSuchElementException.class, mock::getTour);
    }

    /**
     * Tests {@link TourService#getAvailableActions(Tour, Long)} in the successful case with an expected pair of tour and tourist.<br>
     * Ensures that the available actions are correct.
     */
    @Test
    void getAvailableActionsTourist() {
        // setup
        Tour expectedTour = mock.getTour();
        Tourist subject = mock.getTourist();
        // under test
        TourActionsResponseDTO underTest = tourService.getAvailableActions(expectedTour, subject.getId());
        // assertion
        assertTrue(!underTest.getCreateReport() && !underTest.getDelete() && !underTest.getEdit() && !underTest.getMarkAsCompleted() &&
                !underTest.getReview() && underTest.getSendMessage() && !underTest.getViewReports());
    }

    /**
     * Tests {@link TourService#getAvailableActions(Tour, Long)} in the successful case with an expected pair of tour and guide.<br>
     * Ensures that the available actions are correct.
     */
    @Test
    void getAvailableActionsGuide() {
        // setup
        Tour expectedTour = mock.getTour();
        Guide subject = mock.getGuide();
        // under test
        TourActionsResponseDTO underTest = tourService.getAvailableActions(expectedTour, subject.getId());
        // assertion
        assertTrue(!underTest.getCreateReport() && underTest.getDelete() && underTest.getEdit() && !underTest.getMarkAsCompleted() &&
                !underTest.getReview() && !underTest.getSendMessage() && !underTest.getViewReports());
    }

    /**
     * Tests {@link TourService#getAvailableActions(Tour, Long)} in the successful case with an expected pair of tour and administrator.<br>
     * Ensures that the available actions are correct.
     */
    @Test
    void getAvailableActionsAdministrator() {
        // setup
        Tour expectedTour = mock.getTour();
        Administrator subject = mock.getAdministrator();
        // under test
        TourActionsResponseDTO underTest = tourService.getAvailableActions(expectedTour, subject.getId());
        // assertion
        assertTrue(!underTest.getCreateReport() && underTest.getDelete() && !underTest.getEdit() && !underTest.getMarkAsCompleted() &&
                !underTest.getReview() && !underTest.getSendMessage() && underTest.getViewReports());
    }

    /**
     * Tests {@link TourService#getCompletedTours(Tourist)} in the successful case.<br>
     * Ensures that the completed tours are returned except those that are no longer visible.
     */
    @Test
    void getMarkedTours() {
        // setup
        // find a tourist that has at least one marked tour no longer visible (otherwise the test would fail)
        Tourist subject = mock.getAllTourists().stream()
                .filter(t -> t.getMarkedTours().size() > 0 && t.getMarkedTours().stream()
                        .anyMatch(tt -> !tt.isPublic() && !tt.getSharedTourists().contains(t)))
                .findAny().orElseThrow();
        // find the marked tours that are no longer visible
        List<Tour> expectedNotVisible = new ArrayList<>(subject.getMarkedTours());
        expectedNotVisible.removeIf(t -> t.getSharedTourists().contains(subject));
        // under test
        List<Tour> underTest = tourService.getCompletedTours(subject);
        // assertion
        assertEquals(subject.getMarkedTours().size() - expectedNotVisible.size(), underTest.size());    // all marked except those expected not visible
        assertTrue(underTest.stream().noneMatch(expectedNotVisible::contains));
    }


}