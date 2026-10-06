package sk.solver.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WeatherContent(
        uiState = uiState,
        onLoadWeather = viewModel::loadWeather
    )
}