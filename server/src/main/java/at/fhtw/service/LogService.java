package at.fhtw.service;

import at.fhtw.repository.LogRepository;
import at.fhtw.service.mapper.LogMapper;
import at.fhtw.service.model.Log;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public List<Log> getLogsOfTour(int tourId) {
        List<Log> ret = new ArrayList<>();
        for (var logEntity : (logRepository.getLogsOfTour(tourId))) {
            var log = LogMapper.INSTANCE.logEntityToLog(logEntity);
            ret.add(log);
        }
        return ret;
    }
}
