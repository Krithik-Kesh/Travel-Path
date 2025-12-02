package interfaceadapter.reorder_delete_stops;

import entity.ItineraryStop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.reorder_delete_stops.InputBoundary;
import usecase.reorder_delete_stops.InputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//Test suite for ReorderDeleteStopsController.
//Tests multiple input scenarios, null cases, empty lists,
//multiple calls, and ensures InputData is forwarded correctly.
class ReorderDeleteStopsControllerTest {

    private FakeInteractor fakeInteractor;
    private ReorderDeleteStopsController controller;

    @BeforeEach
    void setUp() {
        fakeInteractor = new FakeInteractor();
        controller = new ReorderDeleteStopsController(fakeInteractor);
    }

    // BASIC FORWARDING TESTS

    @Test
    void testControllerForwardsOrderedStopsCorrectly() {
        List<ItineraryStop> list = new ArrayList<>();
        list.add(new ItineraryStop("1", "Toronto", 43, -79, "note"));

        controller.updateOrderedStops(list);

        assertEquals(1, fakeInteractor.callCount);
        assertNotNull(fakeInteractor.receivedInput);
        assertEquals(list, fakeInteractor.receivedInput.getOrderedStops());
    }

    @Test
    void testControllerPassesDifferentListCorrectly() {
        List<ItineraryStop> list = List.of(
                new ItineraryStop("x", "Montreal", 45, -73, "n")
        );

        controller.updateOrderedStops(list);

        assertEquals(1, fakeInteractor.callCount);
        assertEquals("Montreal",
                fakeInteractor.receivedInput.getOrderedStops().get(0).getName());
    }

    // NULL INPUT TESTS

    @Test
    void testControllerAcceptsNullList() {
        controller.updateOrderedStops(null);

        assertEquals(1, fakeInteractor.callCount);
        assertNull(fakeInteractor.receivedInput.getOrderedStops());
    }

    // EMPTY LIST TESTS

    @Test
    void testControllerHandlesEmptyList() {
        List<ItineraryStop> emptyList = List.of();

        controller.updateOrderedStops(emptyList);

        assertEquals(1, fakeInteractor.callCount);
        assertTrue(fakeInteractor.receivedInput.getOrderedStops().isEmpty());
    }

    // MULTIPLE CALLS

    @Test
    void testMultipleSequentialCalls() {
        List<ItineraryStop> list1 = List.of(
                new ItineraryStop("a", "CityA", 10, 10, "n1")
        );

        List<ItineraryStop> list2 = List.of(
                new ItineraryStop("b", "CityB", 20, 20, "n2")
        );

        controller.updateOrderedStops(list1);
        controller.updateOrderedStops(list2);

        assertEquals(2, fakeInteractor.callCount);
        assertEquals("CityB",
                fakeInteractor.receivedInput.getOrderedStops().get(0).getName());
    }

    // EDGE CASES

    @Test
    void testControllerDoesNotModifyGivenList() {
        List<ItineraryStop> list = new ArrayList<>();
        list.add(new ItineraryStop("id", "Original", 0, 0, "note"));

        controller.updateOrderedStops(list);

        // modify original list after call
        list.add(new ItineraryStop("added", "Later", 0, 0, "note"));

        assertEquals(1, fakeInteractor.receivedInput.getOrderedStops().size());
    }

    // FAKE INTERACTOR

    static class FakeInteractor implements InputBoundary {
        InputData receivedInput;
        int callCount = 0;

        @Override
        public void execute(InputData inputData) {
            this.receivedInput = inputData;
            callCount++;
        }
    }
}
