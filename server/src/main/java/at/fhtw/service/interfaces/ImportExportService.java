package at.fhtw.service.interfaces;

import at.fhtw.service.model.ImportExport;

public interface ImportExportService {
    ImportExport exportAllData();

    void importAllData(ImportExport importExport);
}
