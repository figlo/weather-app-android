package sk.solver.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sk.solver.weatherapp.networking.WeatherApiClient
import sk.solver.weatherapp.networking.WeatherClientBuilder
import sk.solver.weatherapp.networking.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository(
        WeatherClientBuilder.create(WeatherApiClient::class.java)
    )

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather(city: String) {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            try {
                val weather = repository.getWeather(
                    city,
                    "metric",
                    WeatherClientBuilder.WEATHER_APP_ID
                )
                _uiState.value = WeatherUiState.Success(weather)
            } catch (e: Exception) {
                _uiState.value = WeatherUiState.Error(
                    e.message ?: "Unknown error"
                )
            }
        }
    }
}