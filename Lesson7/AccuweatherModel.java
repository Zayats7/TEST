package Lesson7;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import javax.xml.soap.Text;
import java.io.IOException;

public class AccuweatherModel implements WeatherModel
{
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day/
    private static final String PROTOKOL = "https";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECASTS = "forecasts";
    private static final String VERSION = "v1";
    private static final String DAILY = "daily";
    private static final String ONE_DAY = "1day";
    private static final String API_KEY = "0dulSRAUoAFQJpJrliFeAMM8RktbIdej";
    private static final String API_KEY_QUERY_PARAM = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";

    //http://dataservice.accuweather.com/forecasts/v1/daily/5day/
    private static final String PROTOKOL5 = "https";
    private static final String BASE_HOST5 = "dataservice.accuweather.com";
    private static final String FORECASTS5 = "forecasts";
    private static final String VERSION5 = "v1";
    private static final String DAILY5 = "daily";
    private static final String FIVE_DAY = "5day";
    private static final String API_KEY5 = "0dulSRAUoAFQJpJrliFeAMM8RktbIdej";
    private static final String API_KEY_QUERY_PARAM5 = "apikey";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void getWeather(String selectedCity, Period period) throws IOException
    {
        switch (period)
        {
            case NOW:
                HttpUrl httpUrl = new HttpUrl.Builder()
                        .scheme(PROTOKOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(ONE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(httpUrl)
                        .build();

                Response oneDayForecastResponse = okHttpClient.newCall(request).execute();
                String weatherResponse = oneDayForecastResponse.body().string();
                System.out.println(weatherResponse);
                break;

            case FIVE_DAYS:
                HttpUrl httpUrl5 = new HttpUrl.Builder()
                        .scheme(PROTOKOL5)
                        .host(BASE_HOST5)
                        .addPathSegment(FORECASTS5)
                        .addPathSegment(VERSION5)
                        .addPathSegment(DAILY5)
                        .addPathSegment(FIVE_DAY)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM5, API_KEY5)
                        .build();

                Request request5 = new Request.Builder()
                        .url(httpUrl5)
                        .get()
                        .addHeader("accept", "application/json")
                        .build();

                Response oneDayForecastResponse5 = okHttpClient.newCall(request5).execute();
                String weatherResponse5 = oneDayForecastResponse5.body().string();

                String weather5 = objectMapper.readTree(weatherResponse5).get(0).at("/Key").asText();
                //return weather5;
                //System.out.println(weatherResponse5);
                System.out.println(weather5);
                break;
        }
    }

    private String detectCityKey(String selectCity) throws IOException
    {
        //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(PROTOKOL)
                .host(BASE_HOST)
                .addPathSegment(LOCATIONS)
                .addPathSegment(VERSION)
                .addPathSegment(CITIES)
                .addPathSegment(AUTOCOMPLETE)
                .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                .addQueryParameter("q", selectCity)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("accept", "application/json")
                .build();

        Response response = okHttpClient.newCall(request).execute();
        String responseString = response.body().string();

        String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();
        return cityKey;
    }
}
