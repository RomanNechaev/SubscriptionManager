package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.impl.export.CSVService;
import ru.matmex.subscription.services.impl.export.JSONService;
import ru.matmex.subscription.services.impl.export.PDFService;

@Controller
public class ExportReportController {
    ExportReportService csvService;
    JSONService jsonService;
    ExportReportService pdfService;

    @Autowired
    public ExportReportController(CSVService csvService, JSONService jsonService, PDFService pdfService) {
        this.csvService = csvService;
        this.jsonService = jsonService;
        this.pdfService = pdfService;
    }

    /**
     * Экспорт отчета в формате CSV
     *
     * @param name - название отчета
     */
    @GetMapping(value = "/api/app/exportCSV/{name}")
    public ResponseEntity<Resource> exportCSV(@PathVariable String name) {
        String filename = "result.csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new ByteArrayResource(csvService.loadReport(name)));
    }

    /**
     * Экспорт отчета в формате JSON
     *
     * @param name - название отчета
     */
    @GetMapping(value = "/api/app/exportJSON/{name}")
    public ResponseEntity<Resource> exportJSON(@PathVariable String name) {
        return ResponseEntity.ok(new ByteArrayResource(jsonService.loadReport(name)));
    }

    /**
     * Экспорт отчета в формате PDF
     *
     * @param name - название отчета
     */
    @GetMapping(value = "/api/app/exportPDF/{name}")
    public ResponseEntity<Resource> exportPDF(@PathVariable String name) {
        String filename = "result.pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(new ByteArrayResource(pdfService.loadReport(name)));
    }
}
