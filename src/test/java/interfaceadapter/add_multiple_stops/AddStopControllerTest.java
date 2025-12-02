package interfaceadapter.add_multiple_stops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.add_stop.AddStopInputBoundary;
import usecase.add_stop.AddStopInputData;

import static org.junit.jupiter.api.Assertions.*;


 //Includes thorough validation of input forwarding, null handling,
 // empty strings, multiple calls, and tracking inside the fake interactor.
class AddStopControllerTest {

    private FakeAddStopInteractor fakeInteractor;
    private AddStopController controller;

    @BeforeEach
    void setUp() {
        fakeInteractor = new FakeAddStopInteractor();
        controller = new AddStopController(fakeInteractor);
    }

    // BASIC FUNCTIONALITY TESTS

    @Test
    void testExecuteForwardsCorrectCityName() {
        String city = "Toronto";

        controller.execute(city);

        assertEquals(1, fakeInteractor.callCount);
        assertNotNull(fakeInteractor.receivedInput);
        assertEquals(city, fakeInteractor.receivedInput.getCityInput());
    }

    @Test
    void testExecuteWithDifferentCity() {
        String city = "Vancouver";

        controller.execute(city);

        assertEquals(1, fakeInteractor.callCount);
        assertEquals("Vancouver", fakeInteractor.receivedInput.getCityInput());
    }

    // NULL & EMPTY INPUT TESTS

    @Test
    void testExecuteWithNullString() {
        controller.execute(null);

        assertEquals(1, fakeInteractor.callCount);
        assertNull(fakeInteractor.receivedInput.getCityInput());
    }

    @Test
    void testExecuteWithEmptyString() {
        controller.execute("");

        assertEquals(1, fakeInteractor.callCount);
        assertEquals("", fakeInteractor.receivedInput.getCityInput());
    }

    @Test
    void testExecuteWithWhitespaceString() {
        controller.execute("   ");

        assertEquals("   ", fakeInteractor.receivedInput.getCityInput());
        assertEquals(1, fakeInteractor.callCount);
    }

    // MULTIPLE CALLS TESTING

    @Test
    void testExecuteCalledMultipleTimes() {
        controller.execute("City1");
        controller.execute("City2");
        controller.execute("City3");

        assertEquals(3, fakeInteractor.callCount);
        assertEquals("City3", fakeInteractor.receivedInput.getCityInput());
    }

    // VALIDATION OF INPUT DATA OBJECT ITSELF

    @Test
    void testInputDataStoresCityNameCorrectly() {
        controller.execute("Tokyo");

        AddStopInputData data = fakeInteractor.receivedInput;

        assertNotNull(data);
        assertEquals("Tokyo", data.getCityInput());
    }

    @Test
    void testInputDataHandlesLongCityNames() {
        String longName = "AveryLongCityNameThatShouldStillBeHandledCorrectly";
        controller.execute(longName);

        assertEquals(longName, fakeInteractor.receivedInput.getCityInput());
    }

    // ERROR HANDLING TESTS
    // (Note: Controller itself does not throw errors; these tests verify that
    //  it still passes the raw values to the interactor.)

    @Test
    void testControllerDoesNotModifyInputEvenIfInvalid() {
        String invalid = "!@#$%^&*()";

        controller.execute(invalid);

        assertEquals(invalid, fakeInteractor.receivedInput.getCityInput());
    }

    @Test
    void testInteractorReceivesExactlyWhatControllerGets() {
        String city = "   Toronto   ";

        controller.execute(city);

        // controller should NOT trim or alter input
        assertEquals(city, fakeInteractor.receivedInput.getCityInput());
    }

    // EXTENDED FAKE INTERACTOR

    //A fake interactor that captures the last received input and counts calls.
    static class FakeAddStopInteractor implements AddStopInputBoundary {

        AddStopInputData receivedInput;
        int callCount = 0;

        @Override
        public void execute(AddStopInputData data) {
            this.receivedInput = data;
            callCount++;
        }
    }
}
