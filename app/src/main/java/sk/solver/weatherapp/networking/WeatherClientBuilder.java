package sk.solver.weatherapp.networking;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class WeatherClientBuilder {
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/";
    public static final String WEATHER_APP_ID = "f00c38e0279b7bc85480c3fe775d518c";

    private static Retrofit instance;

    public static Retrofit getBuilderInstance() {
        if (instance != null) {
            return instance;
        }

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


        instance = new Retrofit.Builder()
                .baseUrl(WEATHER_API_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
//                .addCallAdapterFactory(RxJava3CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(client)
                .build();

        return instance;
    }

    public static <T> T create(final Class<T> serviceInterface) {
        return getBuilderInstance().create(serviceInterface);
    }
}
