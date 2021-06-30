package at.fhtw.service.impl;

import at.fhtw.service.interfaces.ImportExportService;
import at.fhtw.service.interfaces.LogService;
import at.fhtw.service.interfaces.TourService;
import at.fhtw.service.model.ImportExport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImportExportServiceImpl implements ImportExportService {
    private final TourService tourService;
    private final LogService logService;

    @Override
    public ImportExport exportAllData() {
        return ImportExport.builder()
                .logs(logService.getLogs())
                .tours(tourService.getAllDetailedTours()).build();
    }

    @Override
    public void importAllData(ImportExport importExport) {
        for (var tour : importExport.getTours()) {
            tourService.insertOrUpdateTour(tour);
        }
        for (var log : importExport.getLogs()) {
            logService.insertOrUpdateLog(log);
        }
    }
}
