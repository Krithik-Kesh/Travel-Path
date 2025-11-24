package data_access;

import entity.Itinerary;
import use_case.ItineraryRepository;

import java.util.HashMap;
import java.util.Map;


public class InMemoryItineraryRepository implements ItineraryRepository {
    private final Map<String, Itinerary> storage = new HashMap<>();

    @Override
    public Itinerary findById(String id) {
        return storage.get(id);
    }

    @Override
    public void save(Itinerary itinerary) {
        storage.put(itinerary.getId(), itinerary);
    }
}
