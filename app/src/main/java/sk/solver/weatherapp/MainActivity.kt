package sk.solver.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.biometrics.BiometricManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import sk.solver.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cityEditor.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                loadCity();
            }
            return false;
        });
    }

    private void loadCity() {
        WeatherClientBuilder.create(WeatherClient.class)
                .getWeather(binding.cityEditor.getText().toString(), "metric", WeatherClientBuilder.WEATHER_APP_ID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(weatherResponse -> {
                    Toast.makeText(this, "Finished", Toast.LENGTH_LONG).show();

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                    binding.output.setText(objectMapper.writeValueAsString(weatherResponse));
                }, throwable -> {
                    Toast.makeText(this, "Unexpected error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

}