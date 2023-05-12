package ru.matmex.subscription.services.impl.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class CSVService implements ExportReportService {
    UserService userService;
    @Autowired
    public CSVService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public InputStreamResource loadReport(String nameReport) {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        ByteArrayInputStream in = reportToCSV(Report.valueOf(nameReport).calculate(user));

        return new InputStreamResource(in);
    }

    public ByteArrayInputStream reportToCSV(Map<String, Double> report) {
        CSVFormat format = CSVFormat
                .Builder
                .create()
                .setDelimiter(';')
                .build();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (String nameReport : report.keySet()) {
                csvPrinter.printRecord(nameReport, report.get(nameReport));
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
        }
    }
}
