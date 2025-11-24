package interface_adapter.notes;

import use_case.add_note_to_stop.AddNoteToStopInputBoundary;
import use_case.add_note_to_stop.AddNoteToStopInputData;

public class AddNoteToStopController {
    private final AddNoteToStopInputBoundary interactor;

    public AddNoteToStopController(AddNoteToStopInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void addOrUpdateNote(String itineraryId, String stopId, String noteText) {
        AddNoteToStopInputData inputData =
                new AddNoteToStopInputData(itineraryId, stopId, noteText);
        interactor.execute(inputData);
    }
}
