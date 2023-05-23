package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;
import ru.matmex.subscription.repositories.CategoryRepository;
import ru.matmex.subscription.repositories.SubscriptionRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.Parser;
import ru.matmex.subscription.services.utils.mapping.SubscriptionModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Реализация сервиса для операций с подписками
 */
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final SubscriptionModelMapper subscriptionModelMapper;
    private final CategoryRepository categoryRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, SubscriptionModelMapper subscriptionModelMapper,
                                   CategoryService categoryService, UserService userService,
                                   CategoryRepository categoryRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionModelMapper = subscriptionModelMapper;
        this.categoryService = categoryService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Получить спиоск всех подписок текущего пользователя
     *
     * @return список всех подписок текущего пользователя
     */
    @Override
    public List<SubscriptionModel> getSubscriptions() {
        return getSubscriptionsByUser(userService.getCurrentUser())
                .stream()
                .map(subscriptionModelMapper)
                .toList();
    }

    /**
     * Получить список подписок определенного пользователя
     *
     * @param user - сущность пользователя
     * @return список подписок
     */
    public List<Subscription> getSubscriptionsByUser(User user) {
        return categoryRepository
                .findCategoriesByUser(user)
                .orElseThrow(EntityNotFoundException::new)
                .stream().map(Category::getSubscriptions)
                .flatMap(Collection::stream)
                .toList();

    }

    /**
     * Получить подписку
     *
     * @param name имя подписки
     * @return модель подписки
     */
    public SubscriptionModel getSubscription(String name) {
        return getSubscriptions()
                .stream()
                .filter(sub -> Objects.equals(sub.name(), name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("нет подписки с таким названием!"));
    }

    /**
     * Создать подписку
     *
     * @param createSubscriptionModel данные, заполненные пользователем при создании подписки на клиенте
     * @return модель подписки
     */
    @Override
    public SubscriptionModel createSubscription(CreateSubscriptionModel createSubscriptionModel) {
        Category category = categoryService.getCategory(createSubscriptionModel.category());
        Subscription subscription = new Subscription(
                createSubscriptionModel.name(),
                createSubscriptionModel.price(),
                Parser.parseToDate(createSubscriptionModel.paymentDate()),
                category,
                userService.getCurrentUser()
        );
        subscriptionRepository.save(subscription);
        return subscriptionModelMapper.build(subscription);
    }

    /**
     * Удалить подписку
     *
     * @param id - индефикатор подписки в БД
     * @return сообщение об успешном удалении
     */
    @Override
    public String deleteSubscription(Long id) {
        if (subscriptionRepository.existsById(id)) {
            subscriptionRepository.deleteById(id);
            return "Подписка успешна удалена!";
        } else {
            throw new EntityNotFoundException("Нет такой подписки");
        }
    }

    /**
     * Обновить подписку
     *
     * @param updateSubscriptionModel - параметры, заполненные пользователем на клиенте
     * @return модель подписки
     */
    @Override
    public SubscriptionModel updateSubscription(UpdateSubscriptionModel updateSubscriptionModel) {
        Subscription subscription = subscriptionRepository
                .findById(updateSubscriptionModel.id())
                .orElseThrow(EntityNotFoundException::new);
        subscription.setCategory(categoryService.getCategory(updateSubscriptionModel.category()));
        subscription.setName(updateSubscriptionModel.name());
        subscription.setPrice(updateSubscriptionModel.price());
        subscription.setPaymentDate(Parser.parseToDate(updateSubscriptionModel.paymentDate()));
        subscriptionRepository.save(subscription);
        return subscriptionModelMapper.build(subscription);
    }
}
