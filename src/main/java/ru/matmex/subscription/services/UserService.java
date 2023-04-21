package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.User;

public interface UserService {
    User loadByUsername(String username);

    void adduser(User user);
    void updateUsername(Long id, String username);

    void updatePassword(String username, String newPassword);

}
