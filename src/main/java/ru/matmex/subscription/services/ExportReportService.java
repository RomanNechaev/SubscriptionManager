package ru.matmex.subscription.services;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;

public interface ExportReportService {
    //TODO переделать возвращаемый тип
    InputStreamResource loadReport(String nameReport);
}
