package use_case.get_packing_tips;

import entity.WeatherData;
import entity.TipGenerator;
import use_case.WeatherDataAccessInterface;

import java.io.IOException;
import java.util.List;

public class GetPackingTipsInteractor implements GetPackingTipsInputBoundary {
    private final WeatherDataAccessInterface weatherDataAccess;
    private final GetPackingTipsOutputBoundary presenter;

    public GetPackingTipsInteractor(WeatherDataAccessInterface weatherDataAccess,
                                    GetPackingTipsOutputBoundary presenter) {
        this.weatherDataAccess = weatherDataAccess;
        this.presenter = presenter;
    }
    
    @Override
    public void execute(GetPackingTipsInputData inputData) {
        double lat = inputData.latitude();
        double lon = inputData.longitude();

        try {
            WeatherData weather = weatherDataAccess.getCurrentWeather(lat, lon);

            List<String> tips = TipGenerator.generate(weather);

            GetPackingTipsOutputData outputData = new GetPackingTipsOutputData(tips);
            presenter.presentSuccess(outputData);
        } catch (IOException e) {
            presenter.presentFailure("Failed to load weather data: " + e.getMessage());
        }
    }
}
