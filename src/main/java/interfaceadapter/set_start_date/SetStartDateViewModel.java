package interfaceadapter.set_start_date;

import java.time.LocalDate;

public class SetStartDateViewModel {
    // Stores the states of the UI, will be displayed to the user

    private LocalDate startDate;
    private String message;

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
