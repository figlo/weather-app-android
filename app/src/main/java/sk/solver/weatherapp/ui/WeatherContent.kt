package sk.solver.weatherapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.fasterxml.jackson.databind.ObjectMapper
import sk.solver.weatherapp.R
import sk.solver.weatherapp.ui.model.WeatherUiState
import sk.solver.weatherapp.utils.normalized

@Composable
fun WeatherContent(
    uiState: WeatherUiState,
    onLoadWeather: (String) -> Unit
) {
    var city by rememberSaveable(
        stateSaver = TextFieldValue.Saver
    ) {
        mutableStateOf(
            TextFieldValue("")
        )
    }

    var showSuggestions by rememberSaveable {
        mutableStateOf(true)
    }

    val context = LocalContext.current

    val cities = remember {
        context.resources
            .openRawResource(R.raw.cities)
            .bufferedReader()
            .use {
                ObjectMapper()
                    .readValue(it, Array<String>::class.java)
                    .toList()
            }
    }

    val currentCity = city.text
        .substringAfterLast(",")
        .trim()

    val selectedCities = city.text
        .split(",")
        .map { it.trim() }
        .filter { it.isNotBlank() }

    val normalizedCurrentCity = currentCity.normalized()

    val suggestions = if (currentCity.isBlank()) {
        emptyList()
    } else {
        cities
            .filter { suggestion ->
                suggestion.normalized().startsWith(normalizedCurrentCity) &&
                        selectedCities.none {
                            it.normalized() == suggestion.normalized()
                        }
            }
            .take(5)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        city = city.copy(
            selection = TextRange(city.text.length)
        )
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = city,
            onValueChange = {
                city = it
                showSuggestions = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            label = {
                Text(stringResource(R.string.city))
            },
            placeholder = {
                Text(stringResource(R.string.city_placeholder))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (city.text.isNotBlank()) {
                        onLoadWeather(city.text)
                        showSuggestions = false
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                }
            )
        )

        if (showSuggestions && suggestions.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                suggestions.forEach { suggestion ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val prefix = city.text
                                    .substringBeforeLast(",")
                                    .trim()

                                val newCity = if (city.text.contains(",")) {
                                    "$prefix, $suggestion, "
                                } else {
                                    "$suggestion, "
                                }

                                city = TextFieldValue(
                                    text = newCity,
                                    selection = TextRange(newCity.length)
                                )

                                showSuggestions = true
                            },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.outlineVariant
                        ),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = suggestion,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (uiState) {
                WeatherUiState.Idle -> Unit

                is WeatherUiState.Success -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        uiState.weather.forEach { item ->
                            WeatherCard(item)
                        }
                    }
                }
            }
        }
    }
}