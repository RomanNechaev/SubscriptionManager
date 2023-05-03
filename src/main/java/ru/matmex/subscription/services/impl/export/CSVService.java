package ru.matmex.subscription.services.impl.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;
import ru.matmex.subscription.services.ExportReportService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVService implements ExportReportService {
    @Override
    public InputStreamResource loadReport(String nameReport) {
        ByteArrayInputStream in = reportToCSV();
        return new InputStreamResource(in);
    }

    public ByteArrayInputStream reportToCSV() {
        CSVFormat format = CSVFormat
                .Builder
                .create()
                .setDelimiter(';')
                .build();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format))
        {
            //TODO формирование отчета
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
