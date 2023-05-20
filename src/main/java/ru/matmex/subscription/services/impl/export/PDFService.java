package ru.matmex.subscription.services.impl.export;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Создание отчета в формате pdf
 */
@Service
public class PDFService implements ExportReportService {
    UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PDFService.class);

    @Autowired
    public PDFService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public byte[] loadReport(String nameReport) {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        return reportToPDF(Report.valueOf(nameReport).calculate(user));
    }

    /**
     * Формирование отчета в pdf формате
     *
     * @param report отчет(формат: категория -> стоимость всех подписок)
     * @return массив байт, содержащий отчет
     */
    private byte[] reportToPDF(Map<String, Double> report) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try (PDDocument doc = new PDDocument()) {
            PDPage myPage = new PDPage();
            doc.addPage(myPage);
            try (PDPageContentStream cont = new PDPageContentStream(doc, myPage)) {
                cont.beginText();
                cont.setFont(PDType0Font.load(doc, new File("./src/main/resources/fonts/Roboto-Black.ttf")), 12);
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
        return byteArrayOutputStream.toByteArray();
    }
}
