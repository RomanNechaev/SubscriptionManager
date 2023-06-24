package ru.matmex.subscription.services.impl.export.reports;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.DoubleStream;

/**
 * Список всех доступных отчетов
 */
public enum Report {
    AveragePriceCategory {
        /**
         * Высчитывание средней цены по всем категориям пользователя
         *
         * @return категория : средняя цена
         */
        @Override
        public Map<String, Double> calculate(UserModel userModel) {
            Map<String, Double> result = new HashMap<>();
            for (CategoryModel category : userModel.categories()) {
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
    },

    TotalPriceCategory {
        /**
         * Высчитывание суммарной стоимости по всем категориям пользователя
         *
         * @return категория : суммарная стоимость
         */
        public Map<String, Double> calculate(UserModel userModel) {
            Map<String, Double> result = new HashMap<>();
            for (CategoryModel category : userModel.categories()) {
                double[] prices = category
                        .subscriptions()
                        .stream()
                        .mapToDouble(Subscription::getPrice)
                        .toArray();
                result.put(category.name(), DoubleStream.of(prices).sum());
            }
            return result;
        }
    };

    public abstract Map<String, Double> calculate(UserModel userModel);
}
