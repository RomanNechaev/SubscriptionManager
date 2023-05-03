package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.matmex.subscription.services.ExportReportService;

@Controller
public class ExportReportController {
    ExportReportService csvService;

    @Autowired
    public ExportReportController(ExportReportService csvService) {
        this.csvService = csvService;
    }
    /**
     * Экспорт отчета в формате .csv
     *
     * @param name - текущий пользователь
     */
    @GetMapping(value = "/api/app/exportCSV/{name}")
    public ResponseEntity<Resource> exportCSV(@PathVariable String name) {
        String filename = "result.csv";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(csvService.loadReport(name));
    }
}
