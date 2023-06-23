package ru.matmex.subscription.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.services.notifications.Notifiable;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserModel adduser(UserRegistrationModel user);

    UserModel updateUser(UserUpdateModel userUpdateModel);

    UserModel getUserModel(String username);

    User getUser(String username) throws UsernameNotFoundException;

    User getCurrentUser();

    String delete(String username);

    List<UserModel> getUsers();

    boolean checkIntegrationWithTelegram();

    void setTelegramChatId(String username, long telegramChatId);
}
