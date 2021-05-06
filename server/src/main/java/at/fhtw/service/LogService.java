package at.fhtw.service;

import at.fhtw.client.MapQuestClient;
import at.fhtw.repository.LogRepository;
import at.fhtw.service.mapper.LogMapper;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;
    private final MapQuestClient mapQuestClient;

    public List<Log> getLogsOfTour(int tourId) throws IOException {
        List<Log> ret = new ArrayList<>();
        for (var logEntity : (logRepository.getLogsOfTour(tourId))) {
            var log = LogMapper.INSTANCE.logEntityToLog(logEntity);

            var routeResponse = mapQuestClient.getRoute(List.of(log.getStartLocation(), log.getEndLocation()));
            log.setDistance(routeResponse.getRoute().getDistance());

            ret.add(log);
        }
        return ret;
    }
}
