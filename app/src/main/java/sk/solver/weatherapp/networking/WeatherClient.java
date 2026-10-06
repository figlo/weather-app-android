package sk.solver.weatherapp.networking;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sk.solver.weatherapp.models.WeatherResponse;

public interface WeatherClient {

    @GET("weather")
    Observable<WeatherResponse> getWeather(@Query("q") String cities, @Query("units") String units, @Query("appid") String appId);
}
