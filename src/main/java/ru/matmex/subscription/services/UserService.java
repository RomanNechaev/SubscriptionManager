package ru.matmex.subscription.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;

public interface UserService extends UserDetailsService {
    void adduser(UserRegistrationModel user);
    UserModel updateUsername(UserUpdateModel userUpdateModel);

    User getCurrentUser();
}
