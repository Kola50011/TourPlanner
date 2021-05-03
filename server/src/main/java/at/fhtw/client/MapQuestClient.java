package at.fhtw.client;

import at.fhtw.client.model.RouteRequest;
import at.fhtw.client.model.RouteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Slf4j
public class MapQuestClient {
    public static final MediaType JSON_TYPE
            = MediaType.get("application/json; charset=utf-8");
    private static final String KEY = "MRJrIUJ2BwcAJfaA1BiejGn5fV90wDHL";

    private OkHttpClient httpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("https")
            .host("www.mapquestapi.com")
            .build();

    public MapQuestClient() {
        baseUrl = this.baseUrl.newBuilder()
                .addQueryParameter("key", KEY)
                .build();
    }

    public RouteResponse getRoute(List<String> locations) throws IOException {
        var requestBody = objectMapper.writeValueAsString(
                RouteRequest.builder().locations(locations).build()
        );

        var url = baseUrl.newBuilder()
                .addPathSegments("directions/v2/route").build();
        var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestBody.getBytes()))
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make route request with url {}!", url);
            }
            var responseString = response.body().string();
            log.info(responseString);
            return objectMapper.readValue(responseString, RouteResponse.class);
        }
    }

    public String getMap(String session) throws IOException {
        var url = baseUrl.newBuilder()
                .addPathSegments("staticmap/v5/map")
                .addQueryParameter("session", session)
                .build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make map request with url {}!", url);
            }
            return Base64.getEncoder().encodeToString(response.body().bytes());
        }
    }
}
