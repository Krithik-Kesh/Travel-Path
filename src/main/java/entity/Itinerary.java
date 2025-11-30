package entity;

import java.util.ArrayList;
import java.util.List;

public class Itinerary {

    private final String id;
    private final TravelRecord record;
    private final List<ItineraryStop> stops;

    public Itinerary(String id, TravelRecord record, List<ItineraryStop> stops) {
        this.id = id;
        this.record = record;
        if (stops == null) {
            this.stops = new ArrayList<>();
        } else {
            this.stops = new ArrayList<>(stops);
        }
    }

    public String getId() {
        return id;
    }

    public TravelRecord getRecord() {
        return record;
    }

    public List<ItineraryStop> getStops() {
        return stops;
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
