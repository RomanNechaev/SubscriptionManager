package ru.matmex.subscription.services;

/**
 * Сервис для экспорта различных отчетов
 */
public interface ExportReportService {
    /**
     * Загрузка отчета
     *
     * @param nameReport название отчета
     */
    byte[] loadReport(String nameReport);
}
