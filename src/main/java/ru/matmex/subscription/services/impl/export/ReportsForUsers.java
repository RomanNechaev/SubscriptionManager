package ru.matmex.subscription.services.impl.export;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

public class ReportsForUsers {
    UserService userService;

    public ReportsForUsers(UserService userService) {
        this.userService = userService;
    }

    public Map<String, Double> averagePriceCategory() {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);

        Map<String, Double> result = new HashMap<>();
        for (CategoryModel category : user.categories()) {
            double[] prices = category
                    .subscriptions()
                    .stream()
                    .mapToDouble(Subscription::getPrice)
                    .toArray();
            DoubleStream.of(prices).average()
                        .ifPresentOrElse(averagePrice -> result.put(category.name(), averagePrice),
                                        () -> result.put(category.name(), 0.0));
        }
        return result;
    }
    public Map<String, Double> totalPriceCategory() {
        String userName = userService.getCurrentUser().getUsername();
        UserModel user = userService.getUser(userName);

        Map<String, Double> result = new HashMap<>();
        for (CategoryModel category : user.categories()) {
            double[] prices = category
                    .subscriptions()
                    .stream()
                    .mapToDouble(Subscription::getPrice)
                    .toArray();
            result.put(category.name(), DoubleStream.of(prices).sum());
        }
        return result;
    }
}
