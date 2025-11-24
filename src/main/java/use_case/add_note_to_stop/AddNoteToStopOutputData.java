package use_case.add_note_to_stop;
// add not for stop.
public class AddNoteToStopOutputData {
    private final String itineraryId;
    private final String stopId;
    private final String noteText;

    public AddNoteToStopOutputData(String itineraryId, String stopId, String noteText) {
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
