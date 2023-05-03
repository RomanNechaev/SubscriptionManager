package ru.matmex.subscription.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserModel adduser(UserRegistrationModel user);

    UserModel updateUsername(UserUpdateModel userUpdateModel);

    UserModel getUser(String username);

    User getCurrentUser();
    String delete(String username);

    List<UserModel> getUsers();
}
