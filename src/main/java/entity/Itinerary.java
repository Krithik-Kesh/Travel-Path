package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Itinerary {
    private final String id;
    private final String title;
    private final List<ItineraryStop> stops = new ArrayList<>();

    public Itinerary(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<ItineraryStop> getStops() {
        return Collections.unmodifiableList(stops);
    }

    public void addStop(ItineraryStop stop) {
        stops.add(stop);
    }

    public ItineraryStop findStopById(String stopId) {
        for (ItineraryStop stop : stops) {
            if (stop.getId().equals(stopId)) {
                return stop;
            }
        }
        return null;
    }
}
