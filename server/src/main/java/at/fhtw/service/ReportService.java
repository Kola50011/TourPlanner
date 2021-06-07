package at.fhtw.service;

import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.Duration;

@RequiredArgsConstructor
@Service
@Slf4j
public class ReportService {
    private final TourService tourService;
    private final LogService logService;
    private final TemplateEngine templateEngine;

    public static String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        var positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    public byte[] generateReportForTour(int tourId) {
        var optionalTour = tourService.getTour(tourId);
        if (optionalTour.isEmpty()) {
            return new byte[0];
        }

        var tour = optionalTour.get();
        var logs = logService.getLogsOfTour(tourId);

        var context = new Context();
        context.setVariable("name", tour.getName());
        context.setVariable("description", tour.getDescription());
        context.setVariable("distance", tour.getDistance());
        context.setVariable("image", tour.getImage());
        context.setVariable("logs", logs);

        String html = templateEngine.process("templates/tourReportTemplate", context);

        return htmlToPdf(html);
    }

    public byte[] generateLogsReport() {
        var logs = logService.getLogs();

        var totalTime = 0;
        var totalDistance = 0.0;

        for (var log : logs) {
            totalDistance += log.getDistance();
            var duration = Duration.between(log.getStartTime().toInstant(), log.getEndTime().toInstant());
            totalTime += duration.getSeconds();
        }

        var context = new Context();
        context.setVariable("totalTime", formatDuration(Duration.ofSeconds(totalTime)));
        context.setVariable("totalDistance", totalDistance);
        context.setVariable("logs", logs);

        String html = templateEngine.process("templates/summarizeReportTemplate", context);

        return htmlToPdf(html);
    }

    private byte[] htmlToPdf(String html) {
        try {
            var outputStream = new ByteArrayOutputStream();
            var renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (DocumentException e) {
            log.error("Error during PDF generation!", e);
        }
        return new byte[0];
    }
}
