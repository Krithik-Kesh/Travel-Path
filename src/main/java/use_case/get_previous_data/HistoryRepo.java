package use_case.get_previous_data;

import entity.TravelRecord;

import java.util.List;

/**
 * Simple gateway for loading travel history.
 */
public interface HistoryRepo {

    List<TravelRecord> findByUser(String user);
}
