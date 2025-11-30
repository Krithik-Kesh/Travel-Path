package interface_adapter.save_itinerary;

import entity.TravelRecord;
import use_case.save_itinerary.SaveInput;
import use_case.save_itinerary.SaveInputBoundary;

//Very small controller to trigger the save use case.

public class SaveController {

    private final SaveInputBoundary interactor;

    public SaveController(SaveInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void save(TravelRecord record) {
        SaveInput input = new SaveInput(record);
        interactor.execute(input);
    }
}
