package interface_adapter.add_multiple_stops;

public class AddStopController {
    // This is the Interface for your Interactor
    final AddStopInputBoundary addStopUseCaseInteractor;

    // Constructor: "Dependency Injection"
    public AddStopController(AddStopInputBoundary addStopUseCaseInteractor) {
        this.addStopUseCaseInteractor = addStopUseCaseInteractor;
    }

    // The Action: The UI calls this method
    public void execute(String cityName) {
        // Package the raw string into an InputData object
        AddStopInputData inputData = new AddStopInputData(cityName);

        //Pass it to the Interactor
        addStopUseCaseInteractor.execute(inputData);
    }
}