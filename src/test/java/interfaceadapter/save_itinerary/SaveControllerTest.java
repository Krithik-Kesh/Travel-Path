package interfaceadapter.save_itinerary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.save_itinerary.SaveInputBoundary;
import usecase.save_itinerary.SaveInput;

import static org.junit.jupiter.api.Assertions.*;

class SaveControllerTest {

    private FakeSaveInteractor fakeInteractor;
    private SaveController controller;

    @BeforeEach
    void setUp() {
        fakeInteractor = new FakeSaveInteractor();
        controller = new SaveController(fakeInteractor);
    }

    // BASIC FORWARDING

    @Test
    void testControllerForwardsAllFields() {
        controller.execute("Alice", "Sunny", "Jacket", "2024-01-10");

        assertEquals(1, fakeInteractor.callCount);
        assertEquals("Alice", fakeInteractor.lastInput.getUsername());
        assertEquals("Sunny", fakeInteractor.lastInput.getWeatherSummary());
        assertEquals("Jacket", fakeInteractor.lastInput.getClothingSuggestion());
        assertEquals("2024-01-10", fakeInteractor.lastInput.getStartDateInput());
    }

    @Test
    void testControllerAcceptsDifferentValues() {
        controller.execute("Bob", "Snow", "Boots", "2025-03-12");

        assertEquals("Bob", fakeInteractor.lastInput.getUsername());
        assertEquals("Snow", fakeInteractor.lastInput.getWeatherSummary());
        assertEquals("Boots", fakeInteractor.lastInput.getClothingSuggestion());
        assertEquals("2025-03-12", fakeInteractor.lastInput.getStartDateInput());
    }

    // NULL & EMPTY TESTS

    @Test
    void testControllerHandlesNullValues() {
        controller.execute(null, null, null, null);

        assertNull(fakeInteractor.lastInput.getUsername());
        assertNull(fakeInteractor.lastInput.getWeatherSummary());
        assertNull(fakeInteractor.lastInput.getClothingSuggestion());
        assertNull(fakeInteractor.lastInput.getStartDateInput());
    }

    @Test
    void testControllerHandlesEmptyStrings() {
        controller.execute("", "", "", "");

        assertEquals("", fakeInteractor.lastInput.getUsername());
        assertEquals("", fakeInteractor.lastInput.getWeatherSummary());
        assertEquals("", fakeInteractor.lastInput.getClothingSuggestion());
        assertEquals("", fakeInteractor.lastInput.getStartDateInput());
    }

    @Test
    void testControllerHandlesWhitespace() {
        controller.execute("   ", "  ", "   clothes", " 2024-10-10 ");

        assertEquals("   ", fakeInteractor.lastInput.getUsername());
        assertEquals("  ", fakeInteractor.lastInput.getWeatherSummary());
        assertEquals("   clothes", fakeInteractor.lastInput.getClothingSuggestion());
        assertEquals(" 2024-10-10 ", fakeInteractor.lastInput.getStartDateInput());
    }

    // MULTIPLE CALLS

    @Test
    void testControllerMultipleSequentialCalls() {
        controller.execute("User1", "Rain", "Coat", "2023-01-01");
        controller.execute("User2", "Fog", "Scarf", "2023-02-02");

        assertEquals(2, fakeInteractor.callCount);
        assertEquals("User2", fakeInteractor.lastInput.getUsername());
    }

    // EDGE CASES

    @Test
    void testControllerDoesNotModifyInputStrings() {
        String date = "   2024-04-04   ";
        controller.execute("X", "Y", "Z", date);

        assertEquals(date, fakeInteractor.lastInput.getStartDateInput());
    }


    // FAKE INTERACTOR

    static class FakeSaveInteractor implements SaveInputBoundary {

        SaveInput lastInput;
        int callCount = 0;

        @Override
        public void execute(SaveInput input) {
            this.lastInput = input;
            callCount++;
        }
    }
}
