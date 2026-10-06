package sk.solver.weatherapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import sk.solver.weatherapp.ui.model.WeatherItem

@Composable
fun WeatherCard(
    item: WeatherItem
) {
    val backgroundColor = when (item) {
        is WeatherItem.Loading -> Color.LightGray
        is WeatherItem.Error -> Color.LightGray
        is WeatherItem.Success -> temperatureColor(item.weather.main.temp)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            when (item) {
                is WeatherItem.Loading -> {
                    Text(
                        text = item.city,
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text("Loading...")
                    }
                }

                is WeatherItem.Success -> {
                    WeatherCardContent(item.weather)
                }

                is WeatherItem.Error   -> {
                    Text(
                        text = item.city,
                        style = MaterialTheme.typography.displaySmall
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

private fun temperatureColor(temperature: Double): Color {
    val cold = Color(0xFF1976D2)
    val freezing = Color(0xFF90CAF9)
    val neutral = Color(0xFFFFFBFE)
    val warm = Color(0xFFFFCC80)
    val hot = Color(0xFFE53935)

    return when {
        temperature <= -25 -> {
            lerp(
                cold,
                freezing,
                ((temperature + 50) / 25).coerceIn(0.0, 1.0).toFloat()
            )
        }

        temperature <= 0 -> {
            lerp(
                freezing,
                neutral,
                ((temperature + 25) / 25).coerceIn(0.0, 1.0).toFloat()
            )
        }

        temperature <= 25 -> {
            lerp(
                neutral,
                warm,
                (temperature / 25).coerceIn(0.0, 1.0).toFloat()
            )
        }

        else -> {
            lerp(
                warm,
                hot,
                ((temperature - 25) / 25).coerceIn(0.0, 1.0).toFloat()
            )
        }
    }
}
