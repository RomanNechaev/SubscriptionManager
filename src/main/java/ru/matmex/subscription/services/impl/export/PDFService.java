package ru.matmex.subscription.services.impl.export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class PDFService implements ExportReportService {
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PDFService.class);

    @Autowired
    public PDFService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public InputStreamResource loadReport(String nameReport) {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        ByteArrayInputStream in = reportToPDF(Report.valueOf(nameReport).calculate(user));
        return new InputStreamResource(in);
    }

    /**
     * Формирование отчета в pdf формате
     */
    private ByteArrayInputStream reportToPDF(Map<String, Double> report) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (PDDocument doc = new PDDocument()) {
            PDPage myPage = new PDPage();
            doc.addPage(myPage);
            try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {
                cont.beginText();
                cont.setFont(PDType1Font.TIMES_ROMAN, 12);
                cont.setLeading(14.5f);
                cont.newLineAtOffset(25, 700);
                for (String key : report.keySet()) {
                    cont.showText(key + " : " + report.get(key));
                    cont.newLine();
                }
                cont.endText();
            }
            doc.save(byteArrayOutputStream);
        } catch (IOException e) {
            logger.error(String.format("Не удалось создать .pdf файл для экспорта отчетов у %s",
                    userService.getCurrentUser()));
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
