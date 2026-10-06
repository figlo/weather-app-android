package sk.solver.weatherapp.ui.model

sealed interface WeatherUiState {
    data object Idle: WeatherUiState
    data class Success(val weather: List<WeatherItem>): WeatherUiState
}