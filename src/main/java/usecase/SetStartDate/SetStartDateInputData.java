package usecase.SetStartDate;

import java.time.LocalDate;

public class SetStartDateInputData {
    // Input data for the Use Case SetStartDate.
// Data passed from Controller to the Interactor

    private String itineraryId = ""; // This cannot be final, needs to be modified later
    private final LocalDate startDate;

    public SetStartDateInputData(LocalDate startDate) {
        this.itineraryId = itineraryId;
        this.startDate = startDate; // Get start date of the itinerary
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
