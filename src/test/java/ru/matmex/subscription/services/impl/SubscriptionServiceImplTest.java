package ru.matmex.subscription.services.impl;

import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
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
import ru.matmex.subscription.utils.CategoryBuilder;
import ru.matmex.subscription.utils.SubscriptionBuilder;
import ru.matmex.subscription.utils.UserBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {SubscriptionServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class SubscriptionServiceImplTest {
    @Autowired
    private SubscriptionServiceImpl subscriptionServiceImpl;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private UserService userService;
    private final SubscriptionModelMapper subscriptionModelMapper = new SubscriptionModelMapper();
    private SubscriptionService subscriptionService;

    private Subscription defaultSubscription = SubscriptionBuilder.anSubscription().defaultSubscription();

    @BeforeEach
    void setUp() {
        subscriptionService = new SubscriptionServiceImpl(
                subscriptionRepository,
                subscriptionModelMapper,
                categoryService,
                userService);
    }

    @Test
    void testCanGetSubscriptionsByUser() {
        User testUser = UserBuilder.anUser().defaultUser();
        List<Subscription> subscriptionList = List
                .of(defaultSubscription,
                        SubscriptionBuilder.anSubscription()
                                .withName("test2")
                                .withCategory(CategoryBuilder.anCategory().defaultCategory())
                                .withPrice(15.0)
                                .withPaymentDate(Parser.parseToDate("12-03-2023"))
                                .withUser(testUser)
                                .build());

        when(subscriptionRepository.findSubscriptionByUser(testUser)).thenReturn(Optional.of(subscriptionList));
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsByUser(testUser);

        assertThat(subscriptionList.size()).isEqualTo(subscriptions.size());

        verify(subscriptionRepository).findSubscriptionByUser(testUser);

    }

    @Test
    void testCanGetAllSubscriptions() {
        User testUser = UserBuilder.anUser().defaultUser();
        List<Subscription> subscriptionList = List
                .of(defaultSubscription,
                        SubscriptionBuilder.anSubscription()
                                .withName("test2")
                                .withCategory(CategoryBuilder.anCategory().defaultCategory())
                                .withPrice(15.0)
                                .withPaymentDate(Parser.parseToDate("12-03-2023"))
                                .withUser(testUser)
                                .build());
        when(userService.getCurrentUser()).thenReturn(testUser);

        when(subscriptionRepository.findSubscriptionByUser(testUser)).thenReturn(Optional.of(subscriptionList));

        List<SubscriptionModel> subscriptionModelList = subscriptionService.getSubscriptions();
        assertThat(subscriptionList.stream().map(subscriptionModelMapper).toList()).isEqualTo(subscriptionModelList);
    }

    @Test
    void testCanGetSubscriptionByName() {
        User testUser = UserBuilder.anUser().defaultUser();

        List<Subscription> subscriptionList = List
                .of(defaultSubscription,
                        SubscriptionBuilder.anSubscription()
                                .withName("test2")
                                .withCategory(CategoryBuilder.anCategory().defaultCategory())
                                .withPrice(15.0)
                                .withPaymentDate(Parser.parseToDate("12-03-2023"))
                                .withUser(testUser)
                                .build());

        when(userService.getCurrentUser()).thenReturn(testUser);
        when(subscriptionRepository.findSubscriptionByUser(testUser)).thenReturn(Optional.of(subscriptionList));
        SubscriptionModel actualSubscription = subscriptionService.getSubscription(testUser.getUsername());

        assertThat(defaultSubscription.getName()).isEqualTo(actualSubscription.name());
    }

    @Test
    void testCanCreateSubscription() {
        CreateSubscriptionModel createSubscriptionModel = new CreateSubscriptionModel("test", "12-03-2013", 123.0, "Test");
        when(categoryService.getCategory(any())).thenReturn(CategoryBuilder.anCategory().defaultCategory());
        subscriptionService.createSubscription(createSubscriptionModel);

        ArgumentCaptor<Subscription> subscriptionArgumentCaptor = ArgumentCaptor.forClass(Subscription.class);

        verify(subscriptionRepository, times(1)).save(subscriptionArgumentCaptor.capture());

        Subscription subscription = subscriptionArgumentCaptor.getValue();

        assertThat(subscription.getName()).isSameAs(createSubscriptionModel.name());
    }

    @Test
    void testCanDeleteSubscription() {
        Long subscriptionId = 12L;
        given(subscriptionRepository.existsById(subscriptionId)).willReturn(true);

        subscriptionService.deleteSubscription(subscriptionId);

        verify(subscriptionRepository).deleteById(subscriptionId);
    }

    @Test
    void testWillThrowWhenDeleteSubscriptionNotFound() {
        Long subscriptionId = 12L;
        given(subscriptionRepository.existsById(subscriptionId)).willReturn(false);

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(subscriptionId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Нет такой подписки");

        verify(subscriptionRepository, never()).deleteById(any());

    }

    @Test
    void testCanUpdateSubscription() {
        String newName = "spotify";
        Long subscriptionId = 12L;
        UpdateSubscriptionModel updateSubscriptionModel = new UpdateSubscriptionModel(
                subscriptionId,
                newName,
                "12-03-2023",
                123.0,
                "music");
        String oldName = "yandex";
        Subscription subscription = SubscriptionBuilder.anSubscription()
                .withName(oldName)
                .withPrice(123.0)
                .withCategory(CategoryBuilder.anCategory().defaultCategory())
                .withUser(UserBuilder.anUser().defaultUser())
                .withPaymentDate(Parser.parseToDate("12-03-2023"))
                .build();

        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(categoryService.getCategory(any())).thenReturn(CategoryBuilder.anCategory().defaultCategory());
        subscriptionService.updateSubscription(updateSubscriptionModel);

        String updatedName = subscription.getName();

        assertThat(updatedName).isSameAs(newName);
    }
}