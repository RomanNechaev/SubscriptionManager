package ru.matmex.subscription.services.impl.export;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.io.*;
import java.util.Map;
/**
 * Создание отчета в формате CSV
 */
@Service
public class CSVService implements ExportReportService {
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(CSVService.class);

    @Autowired
    public CSVService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public InputStreamResource loadReport(String nameReport){
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        OutputStream in = writeCSVReportToStream(Report.valueOf(nameReport).calculate(user), new ByteArrayOutputStream());
        ByteArrayInputStream bytes = mapToByteArrayInputStream((ByteArrayOutputStream) in);
        return new InputStreamResource(bytes);
    }

    public OutputStream writeCSVReportToStream(Map<String, Double> report, OutputStream stream) {
        CSVFormat format = CSVFormat
                .Builder
                .create()
                .setDelimiter(';')
                .build();
        CSVPrinter csvPrinter;
        try {
            csvPrinter = new CSVPrinter(new PrintWriter(stream), format);
            for (String nameReport : report.keySet()) {
                csvPrinter.printRecord(nameReport, report.get(nameReport));
            }
            csvPrinter.flush();
        } catch (IOException e) {
            logger.error(String.format("Не удалось создать .csv файл для экспорта отчетов у %s",
                    userService.getCurrentUser()));
            throw new RuntimeException(e);
        }
        return stream;
    }

    private ByteArrayInputStream mapToByteArrayInputStream(ByteArrayOutputStream byteArrayOutputStream) {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
