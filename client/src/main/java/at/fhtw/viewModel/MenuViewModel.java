package at.fhtw.viewModel;

import at.fhtw.client.TourPlannerClient;
import at.fhtw.client.model.ImportExport;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class MenuViewModel {
    private final TourPlannerClient tourPlannerClient;
    ObjectMapper objectMapper = new ObjectMapper();

    public void exportData(File outputFile) {
        var export = tourPlannerClient.getExport();
        try {
            objectMapper.writeValue(outputFile, export);
        } catch (IOException e) {
            log.error("Unable to write to file {}", outputFile, e);
        }
    }

    public void importData(File inputFile) {
        try {
            var importExport = objectMapper.readValue(inputFile, ImportExport.class);
            tourPlannerClient.putImport(importExport);
        } catch (IOException e) {
            log.error("Unable to read from file {}", inputFile, e);
        }
    }
}
