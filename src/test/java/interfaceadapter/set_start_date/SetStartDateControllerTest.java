package interfaceadapter.set_start_date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.SetStartDate.SetStartDateInputBoundary;
import usecase.SetStartDate.SetStartDateInputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


//Covers normal flow, null dates, multiple calls, and input forwarding.
class SetStartDateControllerTest {

    private FakeInteractor fakeInteractor;
    private SetStartDateController controller;

    @BeforeEach
    void setUp() {
        fakeInteractor = new FakeInteractor();
        controller = new SetStartDateController(fakeInteractor, "ID123");
    }

    // BASIC FORWARDING TESTS

    @Test
    void testControllerForwardsCorrectDateAndId() {
        LocalDate date = LocalDate.of(2024, 1, 1);

        controller.setStartDate(date);

        assertEquals(1, fakeInteractor.callCount);
        assertEquals(date, fakeInteractor.lastInput.getStartDate());
        assertEquals("ID123", fakeInteractor.lastInput.getItineraryId());
    }

    @Test
    void testControllerHandlesDifferentDates() {
        LocalDate date = LocalDate.of(2030, 12, 25);

        controller.setStartDate(date);

        assertEquals(date, fakeInteractor.lastInput.getStartDate());
        assertEquals("ID123", fakeInteractor.lastInput.getItineraryId());
    }

    // NULL INPUT TESTS

    @Test
    void testControllerAcceptsNullDate() {
        controller.setStartDate(null);

        assertNull(fakeInteractor.lastInput.getStartDate());
        assertEquals("ID123", fakeInteractor.lastInput.getItineraryId());
    }

    // MULTIPLE CALLS

    @Test
    void testMultipleSequentialCalls() {
        controller.setStartDate(LocalDate.of(2022, 5, 5));
        controller.setStartDate(LocalDate.of(2022, 6, 6));
        controller.setStartDate(LocalDate.of(2022, 7, 7));

        assertEquals(3, fakeInteractor.callCount);
        assertEquals(LocalDate.of(2022, 7, 7), fakeInteractor.lastInput.getStartDate());
    }

    // EDGE CASES

    @Test
    void testControllerHandlesExtremeYear() {
        LocalDate farFuture = LocalDate.of(9999, 1, 1);

        controller.setStartDate(farFuture);

        assertEquals(farFuture, fakeInteractor.lastInput.getStartDate());
    }

    @Test
    void testControllerKeepsItineraryIdConstant() {
        controller.setStartDate(LocalDate.now());

        assertEquals("ID123", fakeInteractor.lastInput.getItineraryId());
    }

    // FAKE INTERACTOR

    static class FakeInteractor implements SetStartDateInputBoundary {

        SetStartDateInputData lastInput;
        int callCount = 0;

        @Override
        public void execute(SetStartDateInputData inputData) {
            this.lastInput = inputData;
            callCount++;
        }
    }
}
