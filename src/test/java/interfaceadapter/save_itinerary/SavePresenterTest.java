package interfaceadapter.save_itinerary;

import entity.TravelRecord;
import interfaceadapter.IteneraryViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.save_itinerary.SaveOutput;
import usecase.save_itinerary.SaveOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class SavePresenterTest {

    private FakeViewModel viewModel;
    private SavePresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new FakeViewModel();
        presenter = new SavePresenter(viewModel);
    }

    // SUCCESS CASES

    @Test
    void testPresenterSuccessClearsErrorAndFiresEvent() {
        TravelRecord record = new TravelRecord("user", "A", "B",
                "10 mins", "weather", "distance", "clothing");

        SaveOutput output = new SaveOutput(record);

        presenter.present(output);

        assertEquals("", viewModel.error);
        assertEquals(1, viewModel.fireCount);
        assertTrue(viewModel.eventFired);
    }

    @Test
    void testPresenterSuccessOverwritesPreviousError() {
        viewModel.error = "Old error";

        TravelRecord record = new TravelRecord("u", "o", "d",
                "x", "y", "z", "tips");

        presenter.present(new SaveOutput(record));

        assertEquals("", viewModel.error);
        assertEquals(1, viewModel.fireCount);
    }

    // FAILURE CASES

    @Test
    void testPresenterFailureSetsErrorAndFiresEvent() {
        presenter.prepareFailView("Could not save");

        assertEquals("Could not save", viewModel.error);
        assertEquals(1, viewModel.fireCount);
        assertTrue(viewModel.eventFired);
    }

    @Test
    void testPresenterFailureDoesNotClearValidState() {
        viewModel.error = "";
        presenter.prepareFailView("ERR");

        assertEquals("ERR", viewModel.error);
        assertEquals(1, viewModel.fireCount);
    }

    @Test
    void testPresenterFailureAcceptsEmptyError() {
        presenter.prepareFailView("");

        assertEquals("", viewModel.error);
        assertEquals(1, viewModel.fireCount);
    }

    // EDGE CASES

    @Test
    void testPresenterSuccessWithNullRecordAllowed() {
        presenter.present(new SaveOutput(null));

        // error cleared but record ignored
        assertEquals("", viewModel.error);
        assertEquals(1, viewModel.fireCount);
    }

    @Test
    void testPresenterDoesNotThrowWhenViewModelAlreadyUsed() {
        viewModel.fireCount = 5;

        presenter.prepareFailView("fail");

        assertEquals("fail", viewModel.error);
        assertEquals(6, viewModel.fireCount);
    }

    // EXTENDED FAKE VIEW MODEL

    static class FakeViewModel extends IteneraryViewModel {

        String error;
        boolean eventFired = false;
        int fireCount = 0;

        @Override
        public void setError(String error) {
            this.error = error;
        }

        @Override
        public void firePropertyChanged() {
            eventFired = true;
            fireCount++;
        }
    }
}
