package ru.matmex.subscription.services.impl.export.reports;

import org.junit.jupiter.api.Test;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.services.utils.mapping.CategoryModelMapper;
import ru.matmex.subscription.utils.CategoryBuilder;
import ru.matmex.subscription.utils.SubscriptionBuilder;
import ru.matmex.subscription.utils.UserBuilder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class CalculateAveragePriceCategoryTest {

    CalculateAveragePriceCategory calculate = new CalculateAveragePriceCategory();
    CategoryModelMapper categoryModelMapper = new CategoryModelMapper();

    /**
     * Тестрование на подсчет средней цены всех подписок в категории без подписок
     */
    @Test
    void testCanGetAveragePriceIfCategoryDoesntHaveSubscriptions() {
        Category category = CategoryBuilder.anCategory().defaultCategory();

        UserModel userModel = new UserModel(1L, "test", Stream.of(category).map(categoryModelMapper::map).toList());

        double averagePrice = calculate.apply(userModel).get(category.getName());

        assertThat(averagePrice).isEqualTo(0.0);
    }

    /**
     * Тестирование возможности подсчета среднец цены, если у пользователя нет категорий и подписок соответсвенно
     */
    @Test
    void testCanGetAveragePriceIfUserDoesntHaveCategories() {
        UserModel userModel = new UserModel(1L, "test", new ArrayList<>());

        Map<String, Double> result = calculate.apply(userModel);

        assertThat(result).isEmpty();
    }

    /**
     * Тестирование на корректность подсчета средней стоимости всех подписок в категории
     */
    @Test
    void testCanCorrectGetAveragePrice() {
        User testUser = UserBuilder.anUser().defaultUser();

        Category category = CategoryBuilder.anCategory().defaultCategory();

        Subscription sub1 = SubscriptionBuilder.anSubscription()
                .withName("test")
                .withUser(testUser)
                .withPrice(100.0)
                .withCategory(category)
                .build();

        Subscription sub2 = SubscriptionBuilder.anSubscription()
                .withName("test2")
                .withUser(testUser)
                .withPrice(200.0)
                .withCategory(category)
                .build();

        category.setSubscriptions(List.of(sub1,sub2));

        UserModel userModel = new UserModel(1L, "test", Stream.of(category).map(categoryModelMapper::map).toList());

        double averagePrice = calculate.apply(userModel).get(category.getName());

        assertThat(averagePrice).isEqualTo(150.0);
    }
}
