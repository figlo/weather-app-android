package sk.solver.weatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sk.solver.weatherapp.models.WeatherResponse

@Composable
fun WeatherCard(
    weather: WeatherResponse
) {
    val currentWeather = weather.weather.firstOrNull()
    val main = weather.main
    val wind = weather.wind

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = weather.name,
                style = MaterialTheme.typography.displaySmall
            )

            currentWeather?.let {
                Text(
                    text = it.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${main.temp} °C",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Feels like ${main.feels_like} °C"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Humidity: ${main.humidity}%")
            Text("Pressure: ${main.pressure} hPa")
            Text("Wind: ${wind.speed} m/s")
        }
    }
}