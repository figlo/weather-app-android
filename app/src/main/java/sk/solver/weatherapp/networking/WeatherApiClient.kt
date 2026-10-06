package sk.solver.weatherapp.networking

import retrofit2.http.GET
import retrofit2.http.Query
import sk.solver.weatherapp.models.WeatherResponse

interface WeatherApiClient {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("units") units: String,
        @Query("appid") appId: String
    ): WeatherResponse
}