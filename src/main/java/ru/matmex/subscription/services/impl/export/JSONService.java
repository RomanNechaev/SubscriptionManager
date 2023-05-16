package ru.matmex.subscription.services.impl.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.ExportReportService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.export.reports.Report;

import java.util.Map;
/**
 * Создание отчета в формате JSON
 */
@Service
public class JSONService  {
    UserService userService;

    @Autowired
    public JSONService(UserService userService) {
        this.userService = userService;
    }

    public Map<String, Double> loadReport(String nameReport) {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);
        return Report.valueOf(nameReport).calculate(user);
    }
}
