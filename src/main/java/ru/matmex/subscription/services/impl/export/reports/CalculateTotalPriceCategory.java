package ru.matmex.subscription.services.impl.export.reports;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.DoubleStream;

public class CalculateTotalPriceCategory implements Function<UserModel, Map<String, Double>> {
    /**
     * Высчитывание суммарной стоимости по всем категориям пользователя
     *
     * @return категория : суммарная стоимость
     */
    @Override
    public Map<String, Double> apply(UserModel userModel) {

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
}
