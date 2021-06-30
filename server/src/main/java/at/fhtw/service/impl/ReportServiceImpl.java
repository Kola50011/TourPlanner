package at.fhtw.service.impl;

import at.fhtw.service.interfaces.LogService;
import at.fhtw.service.interfaces.ReportService;
import at.fhtw.service.interfaces.TourService;
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
public class ReportServiceImpl implements ReportService {
    private final TourService tourService;
    private final LogService logService;
    private final TemplateEngine templateEngine;

    @Override
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

    @Override
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
        context.setVariable("totalTime", ReportService.formatDuration(Duration.ofSeconds(totalTime)));
        context.setVariable("totalDistance", totalDistance);
        context.setVariable("logs", logs);

        String html = templateEngine.process("summarizeReportTemplate", context);

        return htmlToPdf(html);
    }

    @Override
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
        context.setVariable("totalTime", ReportService.formatDuration(Duration.ofSeconds(totalTime)));
        context.setVariable("totalDistance", totalDistance);
        context.setVariable("logs", logs);

        return templateEngine.process("summarizeReportTemplate", context);
    }

    private byte[] htmlToPdf(String html) {
        try {
            var file = File.createTempFile("output", ".pdf");
            OutputStream outputStream = new FileOutputStream(file);
            var renderer = new ITextRenderer(20f * 4f / 3f, 20);
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
