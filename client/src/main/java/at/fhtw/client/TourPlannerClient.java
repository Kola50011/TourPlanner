package at.fhtw.client;

import at.fhtw.client.model.DetailedTour;
import at.fhtw.client.model.Log;
import at.fhtw.client.model.Tour;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.Optional;

public interface TourPlannerClient {

    @RequestLine("GET /tours")
    List<Tour> getTours();

    @RequestLine("GET /tours/{tourId}/logs")
    List<Log> getLogsOfTour(@Param("tourId") int tourId);

    @RequestLine("GET /logs/{logId}")
    Optional<Log> getLog(@Param("logId") int tourId);

    @RequestLine("GET /tours/{tourId}")
    Optional<DetailedTour> getTour(@Param("tourId") int tourId);

    @RequestLine("PUT /tours")
    @Headers("Content-Type: application/json")
    void putTour(DetailedTour tour);

    @RequestLine("PUT /logs")
    @Headers("Content-Type: application/json")
    void putLog(Log log);

    @RequestLine("DELETE /tours/{tourId}")
    void deleteTour(@Param("tourId") int tourId);

    @RequestLine("DELETE /logs/{logId}")
    void deleteLog(@Param("logId") int logId);
}
