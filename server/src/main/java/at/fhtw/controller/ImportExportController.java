package at.fhtw.controller;

import at.fhtw.service.interfaces.ImportExportService;
import at.fhtw.service.model.ImportExport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ImportExportController {
    private final ImportExportService importExportService;

    @GetMapping(path = "/export", produces = "application/json")
    public ImportExport export() {
        return importExportService.exportAllData();
    }

    @PutMapping(path = "/import")
    public void putLog(@RequestBody ImportExport importExport) {
        importExportService.importAllData(importExport);
    }
}
