package interface_adapter.notes;

import use_case.add_note_to_stop.AddNoteToStopOutputBoundary;
import use_case.add_note_to_stop.AddNoteToStopOutputData;

// presenter1
public class AddNoteToStopPresenter implements AddNoteToStopOutputBoundary {
    private final NotesViewModel viewModel;

    public AddNoteToStopPresenter(NotesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentSuccess(AddNoteToStopOutputData outputData) {
        viewModel.setErrorMessage("");
        viewModel.setCurrentNoteText(outputData.getNoteText());
    }

    @Override
    public void presentFailure(String errorMessage) {
        viewModel.setErrorMessage(errorMessage);
    }

    public NotesViewModel getViewModel() {
        return viewModel;
    }
}
