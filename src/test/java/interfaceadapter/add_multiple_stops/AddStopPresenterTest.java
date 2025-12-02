package interfaceadapter.add_multiple_stops;

import entity.ItineraryStop;
import entity.RouteInfo;
import interfaceadapter.IteneraryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.add_stop.AddStopOutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Includes additional validation, multiple scenarios,
 * and extended fake ViewModel tracking for verifying presenter behavior.
 */
class AddStopPresenterTest {

    private FakeViewModel viewModel;
    private AddStopPresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new FakeViewModel();
        presenter = new AddStopPresenter(viewModel);
    }

    // SUCCESS CASE TESTS

    @Test
    void testPrepareSuccessViewUpdatesAllViewModelFields() {
        RouteInfo routeInfo = new RouteInfo(10.0, 20.0, "Test Summary");
        ItineraryStop stop1 = new ItineraryStop("1", "Toronto", 43.0, -79.0, "note1");
        ItineraryStop stop2 = new ItineraryStop("2", "Ottawa", 45.0, -75.0, "note2");
        List<ItineraryStop> stops = List.of(stop1, stop2);

        AddStopOutputData output = new AddStopOutputData(stops, routeInfo);
        presenter.prepareSuccessView(output);

        assertEquals(stops, viewModel.stops);
        assertEquals(routeInfo, viewModel.routeInfo);
        assertEquals("", viewModel.error);
        assertTrue(viewModel.propertyChangedFired);
        assertEquals(1, viewModel.fireEventCount);
    }

    @Test
    void testPrepareSuccessViewOverwritesPreviousState() {
        // pre-fill viewModel with unrelated values to ensure overwrite happens
        viewModel.routeInfo = new RouteInfo(999, 999, "OLD");
        viewModel.error = "OLD ERROR";
        viewModel.stops = List.of(
                new ItineraryStop("old", "OldCity", 0, 0, "old")
        );
        viewModel.fireEventCount = 0;

        RouteInfo newRouteInfo = new RouteInfo(50.0, 80.0, "New summary");
        ItineraryStop stop = new ItineraryStop("9", "Montreal", 10.0, 10.0, "n");
        List<ItineraryStop> newStops = List.of(stop);

        AddStopOutputData output = new AddStopOutputData(newStops, newRouteInfo);
        presenter.prepareSuccessView(output);

        assertEquals(newStops, viewModel.stops);
        assertEquals(newRouteInfo, viewModel.routeInfo);
        assertEquals("", viewModel.error); // cleared
        assertEquals(1, viewModel.fireEventCount);
    }

    @Test
    void testSuccessWhenStopsListIsEmpty() {
        RouteInfo info = new RouteInfo(0.0, 0.0, "Empty Route");
        List<ItineraryStop> emptyStops = List.of();

        AddStopOutputData output = new AddStopOutputData(emptyStops, info);
        presenter.prepareSuccessView(output);

        assertEquals(emptyStops, viewModel.stops);
        assertEquals(info, viewModel.routeInfo);
        assertEquals("", viewModel.error);
        assertTrue(viewModel.propertyChangedFired);
    }

    // FAILURE CASE TESTS

    @Test
    void testPrepareFailViewSetsOnlyErrorField() {
        String errorMsg = "Invalid city";

        presenter.prepareFailView(errorMsg);

        assertEquals(errorMsg, viewModel.error);
        assertNull(viewModel.stops);
        assertNull(viewModel.routeInfo);
        assertTrue(viewModel.propertyChangedFired);
        assertEquals(1, viewModel.fireEventCount);
    }

    @Test
    void testFailureDoesNotOverwriteSuccessData() {
        // preload success values
        RouteInfo info = new RouteInfo(10, 10, "Success");
        ItineraryStop s = new ItineraryStop("x", "Test", 1, 1, "test");
        viewModel.stops = List.of(s);
        viewModel.routeInfo = info;

        presenter.prepareFailView("Error occurred");

        // ensure stops + routeInfo remain unchanged
        assertEquals(List.of(s), viewModel.stops);
        assertEquals(info, viewModel.routeInfo);
        assertEquals("Error occurred", viewModel.error);
        assertEquals(1, viewModel.fireEventCount);
    }

    @Test
    void testPrepareFailViewAcceptsEmptyErrorMessage() {
        presenter.prepareFailView("");

        assertEquals("", viewModel.error);
        assertTrue(viewModel.propertyChangedFired);
    }

    // EDGE CASE

    @Test
    void testPrepareSuccessViewWithNullStopsNotAllowed() {
        RouteInfo info = new RouteInfo(1, 1, "Null stop test");

        assertThrows(NullPointerException.class, () ->
                presenter.prepareSuccessView(new AddStopOutputData(null, info))
        );
    }

    @Test
    void testPrepareSuccessViewWithNullRouteInfoNotAllowed() {
        ItineraryStop s = new ItineraryStop("a", "City", 0, 0, "note");
        List<ItineraryStop> stops = List.of(s);

        assertThrows(NullPointerException.class, () ->
                presenter.prepareSuccessView(new AddStopOutputData(stops, null))
        );
    }

    // EXTRA FAKE VIEWMODEL FOR VALIDATION

    static class FakeViewModel extends IteneraryViewModel {

        List<ItineraryStop> stops;
        RouteInfo routeInfo;
        String error;

        boolean propertyChangedFired = false;
        int fireEventCount = 0;

        @Override
        public void setStops(List<ItineraryStop> stops) {
            this.stops = stops;
        }

        @Override
        public void setRouteInfo(RouteInfo routeInfo) {
            this.routeInfo = routeInfo;
        }

        @Override
        public void setError(String error) {
            this.error = error;
        }

        @Override
        public void firePropertyChanged() {
            propertyChangedFired = true;
            fireEventCount++;
        }
    }
}
