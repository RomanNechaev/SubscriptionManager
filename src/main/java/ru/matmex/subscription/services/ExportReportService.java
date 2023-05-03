package ru.matmex.subscription.services;

import org.springframework.core.io.InputStreamResource;
enum
public interface ExportReportService {
    //TODO переделать возвращаемый тип
    public InputStreamResource loadReport(String nameReport);
}
