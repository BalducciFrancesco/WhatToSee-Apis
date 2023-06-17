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

@Transactional
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TourServiceTest {

    private final EntityMock mock;

    private final TourService tourService;

    @PersistenceContext
    private EntityManager entityManager;


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
        stop.setTour(expected);
        // under test
        Tour underTest = tourService.create(expected);
        // assertion
        assertNotNull(underTest.getId());
        assertEquals(expected.getTitle(), underTest.getTitle());
        assertEquals(1, underTest.getStops().size());
    }

    @Test
    void update() {
        // setup
        Tour expected = mock.getTour();
        entityManager.detach(expected);

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

    @Test
    void searchSingleResult() {
        // setup
        Tour expected = mock.getTour();
        TourSearchDTO searchParams = TourSearchDTO.builder()    // should return only 1 result regardless of role
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

    @Test
    void searchMultipleResultsTourist() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any filtering
        Tourist subject = mock.getTourist();

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

    @Test
    void searchMultipleResultsGuide() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any filtering
        Guide subject = mock.getGuide();

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

    @Test
    void searchMultipleResultsAdministrator() {
        // setup
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // shouldn't do any filtering
        Administrator subject = mock.getAdministrator();

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

    @Test
    void isVisibleAsPublic() {
        // setup
        Tour isPublic = mock.getAllTours().stream().filter(Tour::isPublic).findAny().orElseThrow();
        Tourist expectedVisible = mock.getTourist();
        // under test
        boolean underTestVisible = tourService.isVisible(isPublic, expectedVisible.getId());
        // assertion
        assertTrue(underTestVisible);
    }

    @Test
    void isVisibleAsAdministrator() {
        // setup
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Administrator expectedVisible = mock.getAdministrator();
        // under test
        boolean underTestVisible = tourService.isVisible(isNotPublic, expectedVisible.getId());
        // assertion
        assertTrue(underTestVisible);
    }

    @Test
    void isVisibleAsShared() {
        // setup
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

    @Test
    void isVisibleAsAuthor() {
        // setup
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

    @Test
    void isEditableAsAuthor() {
        // setup
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Guide expectedEditable = isNotPublic.getAuthor();
        Guide expectedNotEditable = mock.getAllGuides().stream().filter(g -> !g.getId().equals(expectedEditable.getId())).findAny().orElseThrow();
        // under test
        boolean underTestEditable = tourService.isEditable(isNotPublic, expectedEditable.getId());
        boolean underTestNotEditable = tourService.isEditable(isNotPublic, expectedNotEditable.getId());
        // assertion
        assertTrue(underTestEditable);
        assertFalse(underTestNotEditable);
    }

    @Test
    void isDeletableAsAuthor() {
        // setup
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Guide expectedDeletable = isNotPublic.getAuthor();
        Guide expectedNotDeletable = mock.getAllGuides().stream().filter(g -> !g.getId().equals(expectedDeletable.getId())).findAny().orElseThrow();
        // under test
        boolean underTestDeletable = tourService.isDeletable(isNotPublic, expectedDeletable.getId());
        boolean underTestNotDeletable = tourService.isDeletable(isNotPublic, expectedNotDeletable.getId());
        // assertion
        assertTrue(underTestDeletable);
        assertFalse(underTestNotDeletable);
    }

    @Test
    void isDeletableAsAdministrator() {
        // setup
        Tour isNotPublic = mock.getAllTours().stream().filter(t -> !t.isPublic() && t.getSharedTourists().size() > 0).findAny().orElseThrow();
        Administrator expectedDeletable = mock.getAdministrator();
        // under test
        boolean underTestDeletable = tourService.isVisible(isNotPublic, expectedDeletable.getId());
        // assertion
        assertTrue(underTestDeletable);
    }

    @Test
    void markAsCompleted() {
        // setup
        Tourist subject = mock.getTourist();
        List<Long> expectedMarkedIds = subject.getMarkedTours().stream().map(Tour::getId).toList();
        Tour expectedNotMarked = mock.getAllTours().stream().filter(t -> !expectedMarkedIds.contains(t.getId())).findAny().orElseThrow();
        // under test
        tourService.markAsCompleted(expectedNotMarked, subject);
        // assertion
        assertTrue(expectedNotMarked.getMarkedTourists().stream().map(User::getId).anyMatch(t -> t.equals(subject.getId())));
    }

    @Test
    void noMultipleMarkAsCompleted() {
        // setup
        Tour expectedMarked = mock.getAllTours().stream().filter(t ->  t.getMarkedTourists().size() > 0).findAny().orElseThrow();
        Tourist subject = expectedMarked.getMarkedTourists().stream().findAny().orElseThrow();
        // under test and assertion
        assertThrows(InteractionAlreadyPerformedException.class, () -> tourService.markAsCompleted(expectedMarked, subject));
    }

    @Test
    void delete() {
        // setup
        Tour expected = mock.getTour();
        // under test
        tourService.delete(expected);
        // assertion
        assertThrows(NoSuchElementException.class, mock::getTour);
    }

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

    @Test
    void getMarkedTours() {
        // setup
        Tourist subject = mock.getAllTourists().stream()
                .filter(t -> t.getMarkedTours().size() > 0 && t.getMarkedTours().stream()
                        .anyMatch(tt -> !tt.isPublic() && !tt.getSharedTourists().contains(t)))
                .findAny().orElseThrow();   // tourist that has at least one marked tour no longer visible
        List<Tour> expectedNotVisible = new ArrayList<>(subject.getMarkedTours());
        expectedNotVisible.removeIf(t -> t.getSharedTourists().contains(subject));  // tours that are no longer visibile
        // under test
        List<Tour> underTest = tourService.getCompletedTours(subject);
        // assertion
        assertEquals(subject.getMarkedTours().size() - expectedNotVisible.size(), underTest.size());    // all marked except those expected not visible
        assertTrue(underTest.stream().noneMatch(expectedNotVisible::contains));
    }


}