package usecase.SetStartDate;

// Output Boundary for the SetStartDate use case.
// Implemented by the Presenter.
// Has two methods:
// 1. prepareSuccessView: called when the start date is updated
// 2. prepareFailView: called when the start date cannot be updated
public interface SetStartDateOutputBoundary {

    void prepareSuccessView(SetStartDateOutputData outputData);

    void prepareFailView(String errorMessage);
}
