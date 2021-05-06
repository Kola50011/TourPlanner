package at.fhtw.client;

import at.fhtw.client.model.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class TourPlannerClient {

    private OkHttpClient httpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(8080)
            .build();

    public TourPlannerClient() {
        baseUrl = this.baseUrl.newBuilder()
                .build();
    }

    public List<Tour> getTours() throws IOException {
        var url = baseUrl.newBuilder()
                .addPathSegments("tours").build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make route request with url {}!", url);
            }
            var responseString = response.body().string();
            return Arrays.asList(objectMapper.readValue(responseString, Tour[].class));
        }
    }
}
