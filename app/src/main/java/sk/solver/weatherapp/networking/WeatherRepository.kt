package sk.solver.weatherapp.networking

import sk.solver.weatherapp.models.WeatherResponse

class WeatherRepository(
    private val apiClient: WeatherApiClient
) {
    suspend fun getWeather(city: String, units: String, appId: String): WeatherResponse {
        return apiClient.getWeather(city, units, appId)
    }
}