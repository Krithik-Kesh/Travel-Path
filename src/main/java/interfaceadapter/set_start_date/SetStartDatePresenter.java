package interfaceadapter.set_start_date;

import usecase.SetStartDate.SetStartDateOutputBoundary;
import usecase.SetStartDate.SetStartDateOutputData;

// Presenter
// Converts output data into ViewModel updates.
public class SetStartDatePresenter implements SetStartDateOutputBoundary {
    // Presenter implements the output boundary


    private final SetStartDateViewModel viewModel; // ViewModel that presenter will update

    public SetStartDatePresenter(SetStartDateViewModel viewModel) {
        this.viewModel = viewModel; // Store ViewModel
    }


    public void presentSuccess(SetStartDateOutputData outputData) { // Case Success
        viewModel.setStartDate(outputData.getStartDate()); // Update the ViewModel with the date
        viewModel.setMessage("Start date saved successfully."); // Set a success message for the UI
    }

    public void presentFailure(String errorMessage) { // Case failure
        viewModel.setMessage(errorMessage); // Put failure message into the ViewModel
    }

    @Override
    public void prepareSuccessView(SetStartDateOutputData outputData) {
        // Need by output boundary

    }

    @Override
    public void prepareFailView(String errorMessage) {
        // need by output boundary

    }
}
