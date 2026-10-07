package sk.solver.weatherapp.ui.model

sealed interface WeatherError {
    data object CityNotFound : WeatherError
    data object Unknown : WeatherError
}
