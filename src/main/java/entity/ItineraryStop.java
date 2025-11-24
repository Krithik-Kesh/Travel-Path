package entity;
// 1

public class ItineraryStop {
    private final String id;
    private final String name;
    private final double latitude;
    private final double longitude;
    private String notes;
    public ItineraryStop(String id,
                         String name,
                         double latitude,
                         double longitude,
                         String notes) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
