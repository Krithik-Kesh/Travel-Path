package use_case.add_note_to_stop;
// output bound.
public interface AddNoteToStopOutputBoundary {
    void presentSuccess(AddNoteToStopOutputData outputData);
    void presentFailure(String errorMessage);
}
