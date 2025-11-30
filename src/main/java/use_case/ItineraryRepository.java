package use_case;

public interface ItineraryRepository {
    Itinerary findById(String id);

    void save(Itinerary itinerary);
}
