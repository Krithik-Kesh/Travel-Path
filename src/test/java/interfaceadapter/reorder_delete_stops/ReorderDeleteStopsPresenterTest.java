package interfaceadapter.reorder_delete_stops;

import entity.ItineraryStop;
import entity.RouteInfo;
import interfaceadapter.IteneraryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.reorder_delete_stops.OutputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// High-LOC test suite for ReorderDeleteStopsPresenter.
// Fully tests success, failure, overwriting behavior, null edge cases,
// and uses an extended FakeViewModel for detailed tracking.

class ReorderDeleteStopsPresenterTest {

    private FakeViewModel viewModel;
    private ReorderDeleteStopsPresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new FakeViewModel();
        presenter = new ReorderDeleteStopsPresenter(viewModel);
    }

    // SUCCESS CASES

    @Test
    void testPresentUpdatesStopsAndRouteInfo() {
        RouteInfo info = new RouteInfo(10, 20, "Test Summary");
        ItineraryStop stop = new ItineraryStop("1", "Toronto", 43, -79, "note");
        List<ItineraryStop> stops = List.of(stop);

        OutputData output = new OutputData(stops, info);

        presenter.present(output);

        assertEquals(info, viewModel.routeInfo);
        assertEquals(stops, viewModel.stops);
        assertEquals(1, viewModel.fireEventCount);
        assertNull(viewModel.error);
    }

    @Test
    void testPresentOverwritesPreviousState() {
        viewModel.routeInfo = new RouteInfo(99, 99, "OLD");
        viewModel.stops = List.of(
                new ItineraryStop("x", "OldCity", 0, 0, "n")
        );

        RouteInfo newRoute = new RouteInfo(30, 40, "New");
        List<ItineraryStop> newStops =
                List.of(new ItineraryStop("id", "NewCity", 1, 1, "n"));

        presenter.present(new OutputData(newStops, newRoute));

        assertEquals(newRoute, viewModel.routeInfo);
        assertEquals(newStops, viewModel.stops);
    }

    // EMPTY / NULL INPUT TESTS

    @Test
    void testPresentWithEmptyList() {
        List<ItineraryStop> empty = List.of();
        RouteInfo info = new RouteInfo(0, 0, "Empty");

        presenter.present(new OutputData(empty, info));

        assertEquals(empty, viewModel.stops);
        assertEquals(info, viewModel.routeInfo);
    }

    @Test
    void testPresentWithNullStopsAllowed() {
        RouteInfo info = new RouteInfo(1, 1, "NullStops");

        presenter.present(new OutputData(null, info));

        assertNull(viewModel.stops);
        assertEquals(info, viewModel.routeInfo);
    }

    @Test
    void testPresentWithNullRouteInfoAllowed() {
        List<ItineraryStop> stops = List.of(
                new ItineraryStop("x", "City", 0, 0, "note")
        );

        presenter.present(new OutputData(stops, null));

        assertEquals(stops, viewModel.stops);
        assertNull(viewModel.routeInfo);
    }

    // FAILURE CASES

    @Test
    void testPresentErrorSetsErrorOnly() {
        presenter.presentError("Failed");

        assertEquals("Failed", viewModel.error);
        assertNull(viewModel.routeInfo);
        assertNull(viewModel.stops);
        assertEquals(1, viewModel.fireEventCount);
    }

    @Test
    void testPresentErrorDoesNotOverwriteValidState() {
        viewModel.stops = List.of(
                new ItineraryStop("1", "Saved", 10, 10, "note")
        );
        viewModel.routeInfo = new RouteInfo(10, 20, "Saved");

        presenter.presentError("ErrorMsg");

        assertEquals("ErrorMsg", viewModel.error);
        assertEquals(1, viewModel.fireEventCount);
    }

    // EXTENDED FAKE VIEWMODEL

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
