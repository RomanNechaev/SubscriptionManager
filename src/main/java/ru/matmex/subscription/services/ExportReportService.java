package ru.matmex.subscription.services;

import org.springframework.core.io.InputStreamResource;

public interface ExportReportService {
    //TODO переделать возвращаемый тип
    <T> T loadReport(String nameReport);
}
