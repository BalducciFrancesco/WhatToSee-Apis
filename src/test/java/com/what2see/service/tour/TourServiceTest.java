package com.what2see.service.tour;

import com.what2see.EntityMock;
import com.what2see.dto.tour.TourActionsResponseDTO;
import com.what2see.dto.tour.TourSearchDTO;
import com.what2see.exception.InteractionAlreadyPerformedException;
import com.what2see.model.tour.Stop;
import com.what2see.model.tour.Tour;
import com.what2see.model.user.Administrator;
import com.what2see.model.user.Guide;
import com.what2see.model.user.Tourist;
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
        TourSearchDTO searchParams = TourSearchDTO.builder()
                .themeId(1L)
                .approxDuration("08:00")
                .cityId(1L)
                .tagIds(List.of(1L))
                .build();
        Tour expected = mock.getTour();
        // under test
        List<Tour> underTest = tourService.search(searchParams);
        // assertion
        assertEquals(1, underTest.size());
        assertEquals(expected.getId(), underTest.get(0).getId());
    }

    @Test
    void searchMultipleResults() {
        // setup
        String expectedDuration = "23:59";
        TourSearchDTO searchParams = TourSearchDTO.builder()
                .approxDuration(expectedDuration)
                .build();
        List<Long> expectedIds = mock.getAllTours().stream().filter(t -> t.getApproxDuration().compareTo(expectedDuration) < 0).map(Tour::getId).toList();
        // under test
        List<Tour> underTest = tourService.search(searchParams);
        // assertion
        assertEquals(2, underTest.size());
        assertTrue(underTest.stream().map(Tour::getId).allMatch(expectedIds::contains));
    }

    @Test
    void searchNoPrivateResults() {
        // setup
        List<Long> notExpectedIds = mock.getAllTours().stream().filter(t -> !t.isPublic()).map(Tour::getId).toList();
        TourSearchDTO searchParams = TourSearchDTO.builder().build();   // retrieve all
        // under test
        List<Tour> underTest = tourService.search(searchParams);
        // assertion
        assertEquals(mock.getAllTours().size() - notExpectedIds.size(), underTest.size()); // all except private
        assertTrue(underTest.stream().allMatch(Tour::isPublic));
        assertTrue(underTest.stream().map(Tour::getId).noneMatch(notExpectedIds::contains));
    }

    @Test
    void findById() {
        // setup
        Tour expected = mock.getTour();
        // under test
        Tour underTest = tourService.findById(1L);
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
        List<Long> sharedTouristsIds = isNotPublic.getSharedTourists().stream().map(Tourist::getId).toList();
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
        boolean underTestEditable = tourService.isVisible(isNotPublic, expectedEditable.getId());
        boolean underTestNotEditable = tourService.isVisible(isNotPublic, expectedNotEditable.getId());
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
        boolean underTestDeletable = tourService.isVisible(isNotPublic, expectedDeletable.getId());
        boolean underTestNotDeletable = tourService.isVisible(isNotPublic, expectedNotDeletable.getId());
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
        assertTrue(expectedNotMarked.getMarkedTourists().stream().map(Tourist::getId).anyMatch(t -> t.equals(subject.getId())));
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


}