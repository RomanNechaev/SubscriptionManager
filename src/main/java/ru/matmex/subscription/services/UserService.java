package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;

public interface UserService {
    UserModel loadByUsername(String username);

    void adduser(UserRegistrationModel user);
    UserModel updateUsername(UserUpdateModel userUpdateModel);

    User getCurrentUser();
}
