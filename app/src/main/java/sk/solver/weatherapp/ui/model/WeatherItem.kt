package sk.solver.weatherapp.ui.model

import sk.solver.weatherapp.models.WeatherResponse

sealed interface WeatherItem {

    data class Loading(
        val city: String
    ) : WeatherItem

    data class Success(
        val weather: WeatherResponse
    ) : WeatherItem

    data class Error(
        val city: String,
        val message: String
    ) : WeatherItem
}
