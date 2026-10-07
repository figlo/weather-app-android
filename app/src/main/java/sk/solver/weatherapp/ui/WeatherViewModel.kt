package sk.solver.weatherapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sk.solver.weatherapp.networking.WeatherApiClient
import sk.solver.weatherapp.networking.WeatherClientBuilder
import sk.solver.weatherapp.networking.WeatherRepository
import sk.solver.weatherapp.ui.model.WeatherItem
import sk.solver.weatherapp.ui.model.WeatherUiState
import sk.solver.weatherapp.utils.getWeatherError
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class WeatherViewModel : ViewModel() {

    private val repository = WeatherRepository(
        WeatherClientBuilder.create(WeatherApiClient::class.java)
    )

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Idle)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadWeather(cities: String) {
        viewModelScope.launch {
            val cityList = cities
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            _uiState.value = WeatherUiState.Success(
                weather = cityList.map { WeatherItem.Loading(it) }
            )

            cityList.forEach { city ->
                launch {
                    delay(Random.nextLong(500, 5000).milliseconds)

                    try {
                        val weather = repository.getWeather(
                            city = city,
                            units = "metric",
                            appId = WeatherClientBuilder.WEATHER_APP_ID
                        )

                        _uiState.update { state ->
                            if (state is WeatherUiState.Success) {
                                state.copy(
                                    weather = state.weather.map { item ->
                                        if (item is WeatherItem.Loading &&
                                            item.city == city
                                        ) {
                                            WeatherItem.Success(weather)
                                        } else {
                                            item
                                        }
                                    }
                                )
                            } else {
                                state
                            }
                        }
                    } catch (e: Exception) {
                        _uiState.update { state ->
                            if (state is WeatherUiState.Success) {
                                state.copy(
                                    weather = state.weather.map { item ->
                                        if (item is WeatherItem.Loading &&
                                            item.city == city
                                        ) {
                                            WeatherItem.Error(
                                                city = city,
                                                weatherError = getWeatherError(e)
                                            )
                                        } else {
                                            item
                                        }
                                    }
                                )
                            } else {
                                state
                            }
                        }
                    }
                }
            }
        }
    }
}