package data_access;

import WeatheronmapAPI.OpenWeathermapApiCaller;
import WeatheronmapAPI.WeatherRequest;
import entity.DailyWeather;
import entity.WeatherData;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.WeatherDataAccessInterface;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherWeatherDataAccess implements WeatherDataAccessInterface {
    private final OpenWeathermapApiCaller apiCaller;

    public OpenWeatherWeatherDataAccess(OpenWeathermapApiCaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    @Override
    public WeatherData getCurrentWeather(double lat, double lon) throws IOException {
        WeatherRequest req = new WeatherRequest(lat, lon);
        String json = apiCaller.getJson(req);

        JSONObject root = new JSONObject(json);
        JSONObject current = root.getJSONObject("current");

        double temp = current.getDouble("temp");
        double windSpeed = current.getDouble("wind_speed");

        JSONArray weatherArray = current.getJSONArray("weather");
        String description = weatherArray.getJSONObject(0).getString("description");

        double precipitation = 0.0;
        if (current.has("rain") && current.getJSONObject("rain").has("1h")) {
            precipitation += current.getJSONObject("rain").getDouble("1h");
        }
        if (current.has("snow") && current.getJSONObject("snow").has("1h")) {
            precipitation += current.getJSONObject("snow").getDouble("1h");
        }

        return new WeatherData(temp, description, windSpeed, precipitation);
    }

    @Override
    public List<DailyWeather> getDailyForecast(double lat, double lon) throws IOException {
        WeatherRequest req = new WeatherRequest(lat, lon);
        String json = apiCaller.getJson(req);

        JSONObject root = new JSONObject(json);
        JSONArray dailyArray = root.getJSONArray("daily");

        List<DailyWeather> result = new ArrayList<>();

        for (int i = 0; i < dailyArray.length(); i++) {
            JSONObject d = dailyArray.getJSONObject(i);

            long dt = d.getLong("dt");
            LocalDate date = Instant.ofEpochSecond(dt)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate();

            JSONObject tempObj = d.getJSONObject("temp");
            double min = tempObj.getDouble("min");
            double max = tempObj.getDouble("max");

            JSONArray weatherArray = d.getJSONArray("weather");
            String description = weatherArray.getJSONObject(0).getString("description");

            result.add(new DailyWeather(date, min, max, description));
        }

        return result;
    }
}
