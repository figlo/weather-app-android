package sk.solver.weatherapp.utils

import retrofit2.HttpException
import sk.solver.weatherapp.ui.model.WeatherError

fun getWeatherError(e: Exception): WeatherError {
    return if (e is HttpException && e.code() == 404) {
        WeatherError.CityNotFound
    } else {
        WeatherError.Unknown
    }
}
