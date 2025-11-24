package use_case.add_note_to_stop;
// inputbound.
public class AddNoteToStopInputData {
    private final String itineraryId;
    private final String stopId;
    private final String noteText;

    public AddNoteToStopInputData(String itineraryId, String stopId, String noteText) {
        this.itineraryId = itineraryId;
        this.stopId = stopId;
        this.noteText = noteText;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public String getStopId() {
        return stopId;
    }

    public String getNoteText() {
        return noteText;
    }
}
