package interface_adapter.notes;

public class NotesViewModel {
    private String currentNoteText = "";
    private String errorMessage = "";

    public String getCurrentNoteText() {
        return currentNoteText;
    }

    public void setCurrentNoteText(String currentNoteText) {
        this.currentNoteText = currentNoteText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean hasError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }
}
