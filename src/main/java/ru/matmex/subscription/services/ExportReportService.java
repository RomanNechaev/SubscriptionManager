package ru.matmex.subscription.services;

public interface ExportReportService {
    /**
     * Загрузить отчет
     * @param nameReport - имя отчета
     * @return отчет в виде массива байт
     */
    byte[] loadReport(String nameReport);
}
