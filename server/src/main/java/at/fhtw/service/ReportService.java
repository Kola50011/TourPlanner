package at.fhtw.service;

import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
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

        String html = templateEngine.process("tourReportTemplate", context);

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

        String html = templateEngine.process("summarizeReportTemplate", context);

        return htmlToPdf(html);
    }

    public String generateLogsReportHtml() {
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

        String html = templateEngine.process("summarizeReportTemplate", context);

        return html;
    }

    private byte[] htmlToPdf(String html) {
        try {
            var file = File.createTempFile("output", ".pdf");
            OutputStream outputStream = new FileOutputStream(file);
            ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
            var path = new ClassPathResource("/static/").getURL().toExternalForm();
            renderer.setDocumentFromString(html, path);
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.close();
            file.deleteOnExit();

            return Files.readAllBytes(file.toPath());
        } catch (DocumentException | IOException e) {
            log.error("Error during PDF generation!", e);
        }
        return new byte[0];
    }
}
