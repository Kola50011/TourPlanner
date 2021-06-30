package at.fhtw.service.interfaces;

import java.time.Duration;

public interface ReportService {
    static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        var positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    byte[] generateReportForTour(int tourId);

    byte[] generateLogsReport();

    String generateLogsReportHtml();
}
