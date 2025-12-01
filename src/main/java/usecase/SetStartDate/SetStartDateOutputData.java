package usecase.SetStartDate;

import java.time.LocalDate;

// Output data for Use Case SetStartDate
// Pass data from Interactor to the Presenter
public class SetStartDateOutputData {

    private final String itineraryId;
    private final LocalDate startDate;

    public SetStartDateOutputData(String itineraryId, LocalDate startDate) {
        this.itineraryId = itineraryId;
        this.startDate = startDate;
    }

    public String getItineraryId() {
        return itineraryId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
