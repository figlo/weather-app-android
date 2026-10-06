package sk.solver.weatherapp.ui

import sk.solver.weatherapp.models.WeatherResponse

sealed interface WeatherUiState {
    data object Idle: WeatherUiState
    data object Loading: WeatherUiState
    data class Success(val weather: WeatherResponse): WeatherUiState
    data class Error(val message: String): WeatherUiState
}