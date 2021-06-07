package at.fhtw.service;

import at.fhtw.service.model.ImportExport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportExportService {
    private final TourService tourService;
    private final LogService logService;

    public ImportExport exportAllData() {
        return ImportExport.builder()
                .logs(logService.getLogs())
                .tours(tourService.getAllDetailedTours()).build();
    }

    public void importAllData(ImportExport importExport) {
        for (var tour : importExport.getTours()) {
            tourService.insertOrUpdateTour(tour);
        }
        for (var log : importExport.getLogs()) {
            logService.insertOrUpdateLog(log);
        }
    }
}
