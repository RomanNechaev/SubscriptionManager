package ru.matmex.subscription.services.impl.export;

import jakarta.persistence.EntityExistsException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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
    public InputStreamResource loadReport(String nameReport) {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        PDDocument pdDocument = Optional.ofNullable(reportToPDF(Report.valueOf(nameReport).calculate(user))).orElseThrow(NullPointerException::new);
        ByteArrayInputStream bytes = mapToByteArrayInputStream(pdDocument);
        return new InputStreamResource(bytes);
    }

    /**
     * Формирование отчета в pdf формате
     */
    private PDDocument reportToPDF(Map<String, Double> report) {
        try (PDDocument pdfDocument = new PDDocument()) {
            PDPage myPage = new PDPage();
            pdfDocument.addPage(myPage);
            try (PDPageContentStream cont = new PDPageContentStream(pdfDocument, myPage)) {
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
            return pdfDocument;
        } catch (IOException e) {
            logger.error(String.format("Не удалось создать .pdf файл для экспорта отчетов у %s",
                    userService.getCurrentUser()));
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Преобразовать документ в массив байт
     *
     * @param pdfDocument отчет в формате pdf документа
     * @return массив байт
     */
    private ByteArrayInputStream mapToByteArrayInputStream(PDDocument pdfDocument) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            pdfDocument.save(byteArrayOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }
}
