package at.fhtw.client;

import at.fhtw.client.model.BoundingBox;
import at.fhtw.client.model.RouteRequest;
import at.fhtw.client.model.RouteResponse;
import at.fhtw.properties.MapQuestProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class MapQuestClient {
    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final HttpUrl baseUrl;

    public MapQuestClient(MapQuestProperties mapQuestProperties) {
        baseUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(mapQuestProperties.getEndpoint())
                .addQueryParameter("key", mapQuestProperties.getSecretKey())
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

    public byte[] getMap(String session, BoundingBox boundingBox, String locations) throws IOException {
        var url = baseUrl.newBuilder()
                .addPathSegments("staticmap/v5/map")
                .addQueryParameter("session", session)
                .addQueryParameter("zoom", "10")
                .addQueryParameter("size", "600,400")
                .addQueryParameter("boundingBox",
                        boundingBox.getUl().getLat() + "," +
                                boundingBox.getUl().getLng() + "," +
                                boundingBox.getLr().getLat() + "," +
                                boundingBox.getLr().getLng())
                .addQueryParameter("locations", locations)
                .build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make map request with url {}!", url);
            }
            return response.body().bytes();
        }
    }
}
