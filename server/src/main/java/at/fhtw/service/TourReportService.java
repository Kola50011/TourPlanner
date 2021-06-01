package at.fhtw.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@RequiredArgsConstructor
@Service
@Slf4j
public class TourReportService {
    private final TourService tourService;
    private final LogService logService;

    @SneakyThrows
    public byte[] generateReportForTour(int tourId) {
        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        var optionalTour = tourService.getTour(tourId);
        var tour = optionalTour.get();
        var logs = logService.getLogsOfTour(tourId);

        var context = new Context();
        context.setVariable("name", tour.getName());
        context.setVariable("description", tour.getDescription());
        context.setVariable("distance", tour.getDistance());
        context.setVariable("image", tour.getImage());
        context.setVariable("logs", logs);

        String html = templateEngine.process("templates/tourTemplate", context);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        return outputStream.toByteArray();
    }
}
