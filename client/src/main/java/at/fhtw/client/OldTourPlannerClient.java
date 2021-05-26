package at.fhtw.client;

import at.fhtw.client.model.DetailedTour;
import at.fhtw.client.model.Log;
import at.fhtw.client.model.Tour;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
public class OldTourPlannerClient {

    private OkHttpClient httpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();

    private HttpUrl baseUrl = new HttpUrl.Builder()
            .scheme("http")
            .host("127.0.0.1")
            .port(8080)
            .build();

    public OldTourPlannerClient() {
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

    public List<Log> getLogs(int tourId) {
        var url = baseUrl.newBuilder()
                .addPathSegments("logs")
                .addPathSegments(Integer.toString(tourId))
                .build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make logs request with url {}!", url);
            }
            var responseString = response.body().string();
            return Arrays.asList(objectMapper.readValue(responseString, Log[].class));
        } catch (IOException e) {
            log.error("Could not make logs request with url {}!", url, e);
        }
        return List.of();
    }

    public Optional<DetailedTour> getTour(int id) {
        var url = baseUrl.newBuilder()
                .addPathSegments("tours")
                .addPathSegments(Integer.toString(id))
                .build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make route request with url {}!", url);
                return Optional.empty();
            }
            var responseString = response.body().string();
            return Optional.of(objectMapper.readValue(responseString, DetailedTour.class));
        } catch (IOException e) {
            log.error("Exception when trying to get tour!", e);
            return Optional.empty();
        }
    }

    public Optional<DetailedTour> getLog(int id) {
        var url = baseUrl.newBuilder()
                .addPathSegments("tours")
                .addPathSegments(Integer.toString(id))
                .build();
        var request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make route request with url {}!", url);
                return Optional.empty();
            }
            var responseString = response.body().string();
            return Optional.of(objectMapper.readValue(responseString, DetailedTour.class));
        } catch (IOException e) {
            log.error("Exception when trying to get tour!", e);
            return Optional.empty();
        }
    }

    public boolean putTour(DetailedTour tour) {
        var url = baseUrl.newBuilder()
                .addPathSegments("tours")
                .build();

        try {
            var request = new Request.Builder()
                    .url(url)
                    .put(RequestBody.create(objectMapper.writeValueAsBytes(tour)))
                    .build();

            try (var response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("Could not put tours with url {}!", url);
                    return false;
                }
            }
        } catch (IOException e) {
            log.error("Exception when trying to put tour!", e);
            return false;
        }
        return true;
    }

    public void deleteTour(int id) {
        var url = baseUrl.newBuilder()
                .addPathSegments("tours")
                .addPathSegments(Integer.toString(id))
                .build();
        var request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        try (var response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                log.error("Could not make delete tour request with url {}!", url);
            }
        } catch (IOException e) {
            log.error("Exception when trying to delete tour!", e);
        }
    }
}
