package interfaceadapter.set_start_date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usecase.SetStartDate.SetStartDateOutputData;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

// Covers success, failure, previous state overwrite, null cases
// Verifies ViewModel state changes + message updates
class SetStartDatePresenterTest {

    private FakeViewModel viewModel;
    private SetStartDatePresenter presenter;

    @BeforeEach
    void setUp() {
        viewModel = new FakeViewModel();
        presenter = new SetStartDatePresenter(viewModel);
    }

    // SUCCESS CASES

    @Test
    void testPrepareSuccessViewUpdatesStartDateAndMessage() {
        LocalDate date = LocalDate.of(2024, 4, 10);
        SetStartDateOutputData output =
                new SetStartDateOutputData("ID123", date);

        presenter.prepareSuccessView(output);

        assertEquals(date, viewModel.startDate);
        assertEquals("Start date saved successfully.", viewModel.message);
    }

    @Test
    void testSuccessOverwritesPreviousMessage() {
        viewModel.message = "OLD-MESSAGE";

        SetStartDateOutputData output =
                new SetStartDateOutputData("ID123", LocalDate.of(2030, 1, 1));

        presenter.prepareSuccessView(output);

        assertEquals("Start date saved successfully.", viewModel.message);
    }

    @Test
    void testSuccessOverwritesPreviousDate() {
        viewModel.startDate = LocalDate.of(2000, 1, 1);

        LocalDate newDate = LocalDate.of(2040, 2, 2);
        presenter.prepareSuccessView(
                new SetStartDateOutputData("ID123", newDate));

        assertEquals(newDate, viewModel.startDate);
    }

    // FAILURE CASES

    @Test
    void testPrepareFailViewSetsMessageOnly() {
        presenter.prepareFailView("ERROR!");

        assertEquals("ERROR!", viewModel.message);
        assertNull(viewModel.startDate);
    }

    @Test
    void testFailureDoesNotOverwriteDate() {
        viewModel.startDate = LocalDate.of(2025, 3, 3);

        presenter.prepareFailView("Something went wrong.");

        assertEquals(LocalDate.of(2025, 3, 3), viewModel.startDate);
        assertEquals("Something went wrong.", viewModel.message);
    }

    @Test
    void testFailureAcceptsEmptyMessage() {
        presenter.prepareFailView("");

        assertEquals("", viewModel.message);
    }

    @Test
    void testFailureAcceptsNullMessage() {
        presenter.prepareFailView(null);

        assertNull(viewModel.message);
    }

    // EDGE CASES

    @Test
    void testSuccessHandlesNullDateGracefully() {
        presenter.prepareSuccessView(
                new SetStartDateOutputData("ID", null));

        assertNull(viewModel.startDate);
        assertEquals("Start date saved successfully.", viewModel.message);
    }

    @Test
    void testPresenterDoesNotCrashWithExtremeDate() {
        LocalDate extreme = LocalDate.of(9999, 12, 31);

        presenter.prepareSuccessView(
                new SetStartDateOutputData("ID", extreme));

        assertEquals(extreme, viewModel.startDate);
    }

    // FAKE VIEW MODEL

    static class FakeViewModel extends SetStartDateViewModel {

        LocalDate startDate;
        String message;

        @Override
        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        @Override
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
