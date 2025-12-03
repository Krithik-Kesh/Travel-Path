package usecase.save_itinerary;

import entity.Itinerary;
import entity.ItineraryStop;
import entity.RouteInfo;
import entity.TravelRecord;
import interfaceadapter.reorder_delete_stops.RouteDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaveInteractorTest {

    /**
     * Simple in-memory implementation of RouteDataAccessInterface for testing.
     */
    private static class TestRouteDataAccess implements RouteDataAccessInterface {
        final List<ItineraryStop> stops = new ArrayList<>();
        Itinerary savedItinerary;
        final boolean throwOnGetRoute;
        LocalDate startDate;

        TestRouteDataAccess(boolean throwOnGetRoute) {
            this.throwOnGetRoute = throwOnGetRoute;
            stops.add(new ItineraryStop("1", "Origin", 1.0, 2.0, ""));
            stops.add(new ItineraryStop("2", "Destination", 3.0, 4.0, ""));
        }

        @Override
        public void addStop(ItineraryStop stop) {
            stops.add(stop);
        }

        @Override
        public List<ItineraryStop> getStops() {
            return stops;
        }

        @Override
        public RouteInfo getRoute(List<ItineraryStop> ignored) {
            if (throwOnGetRoute) {
                throw new RuntimeException("boom");
            }
            return new RouteInfo(10.0, 30.0, "Total: 10.0 km, 30 min");
        }

        @Override
        public void saveItinerary(Itinerary itinerary) {
            this.savedItinerary = itinerary;
        }

        @Override
        public List<Itinerary> loadItineraries() {
            return new ArrayList<>();
        }

        @Override
        public void setStartDate(LocalDate date) {
            this.startDate = date;
        }

        @Override
        public LocalDate getStartDate() {
            return startDate;
        }
    }

    /**
     * Test presenter that records both success and failure calls.
     */
    private static class TestPresenter implements SaveOutputBoundary {
        SaveOutput lastOutput = null;
        String lastErrorMessage = null;

        @Override
        public void present(SaveOutput output) {
            this.lastOutput = output;
        }

        @Override
        public void prepareFailView(String error) {
            this.lastErrorMessage = error;
        }
    }

    @Test
    void execute_savesItineraryAndCallsPresenter() {
        TestRouteDataAccess dao = new TestRouteDataAccess(false);
        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "Alice",
                "Sunny",
                "T-shirt",
                "2024-12-03"
        );

        interactor.execute(input);

        assertNotNull(dao.savedItinerary);
        assertEquals(LocalDate.parse("2024-12-03"), dao.startDate);

        TravelRecord record = dao.savedItinerary.getRecord();
        assertNotNull(record);
        assertEquals("Alice", record.getUsername());
        assertEquals("Origin", record.getOrigin());
        assertEquals("Destination", record.getDestination());
        assertEquals("Current Weather: Sunny", record.getWeatherSummary());
        assertEquals("Total Distance: 10.0 km", record.getOptimalPath());
        assertEquals("Clothing Tips: T-shirt", record.getClothingSuggestion());

        assertNotNull(presenter.lastOutput);
        assertSame(record, presenter.lastOutput.getRecord());
        assertNull(presenter.lastErrorMessage);
    }

    @Test
    void execute_handlesExceptionGracefully() {
        TestRouteDataAccess dao = new TestRouteDataAccess(true);
        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "Bob",
                "Cloudy",
                "Jacket",
                "2024-12-03"
        );

        assertDoesNotThrow(() -> interactor.execute(input));

        assertNull(dao.savedItinerary);
        assertNull(presenter.lastOutput);
        assertNull(presenter.lastErrorMessage);
    }

    @Test
    void execute_returnsFailWhenDateMissing() {
        TestRouteDataAccess dao = new TestRouteDataAccess(false);
        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "Alice",
                "Sunny",
                "T-shirt",
                ""
        );

        interactor.execute(input);

        assertNull(dao.savedItinerary);
        assertNull(presenter.lastOutput);
        assertEquals("Please insert a start date.", presenter.lastErrorMessage);
    }

    @Test
    void execute_returnsFailWhenNullDate() {
        TestRouteDataAccess dao = new TestRouteDataAccess(false);
        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "NullUser",
                "Sunny",
                "T-shirt",
                null
        );

        interactor.execute(input);

        assertNull(dao.savedItinerary);
        assertNull(presenter.lastOutput);
        assertEquals("Please insert a start date.", presenter.lastErrorMessage);
    }

    @Test
    void execute_returnsEarlyWhenDateFormatInvalid() {
        TestRouteDataAccess dao = new TestRouteDataAccess(false);
        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "Alice",
                "Sunny",
                "T-shirt",
                "2024/12/03"
        );

        interactor.execute(input);

        assertNull(dao.savedItinerary);
        assertNull(presenter.lastOutput);
        assertNull(presenter.lastErrorMessage);
    }

    @Test
    void execute_usesUnknownWhenNoStops() {
        TestRouteDataAccess dao = new TestRouteDataAccess(false);
        dao.stops.clear();

        TestPresenter presenter = new TestPresenter();
        SaveInteractor interactor = new SaveInteractor(dao, presenter);

        SaveInput input = new SaveInput(
                "Charlie",
                "Rainy",
                "Coat",
                "2024-12-05"
        );

        interactor.execute(input);

        assertNotNull(dao.savedItinerary);
        TravelRecord record = dao.savedItinerary.getRecord();
        assertEquals("Unknown", record.getOrigin());
        assertEquals("Unknown", record.getDestination());
    }

    @Test
    void constructor_withConcreteTypes_isCallable() {
        SaveInteractor interactor =
                new SaveInteractor(null, (interfaceadapter.save_itinerary.SavePresenter) null);
        assertNotNull(interactor);
    }
}
