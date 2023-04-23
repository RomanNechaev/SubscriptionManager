package ru.matmex.subscription.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.repositories.UserRepository;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.MappingUtils;

@Component
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserModel loadByUsername(String username) {
        return MappingUtils.mapToUserModel(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with name:" + username + "not found")));
    }

    @Override
    public void addUser(UserRegistrationModel userRegistrationModel) {
        userRepository.findByUsername(userRegistrationModel.getUsername()).orElse(userRepository.save(MappingUtils.mapToUserEntity(userRegistrationModel)));
    }

    @Override
    public UserModel updateUsername(UserUpdateModel userUpdateModel) {
        User user = userRepository.getById(userUpdateModel.getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(userUpdateModel.getUsername());
        user.setEmail(userUpdateModel.getEmail());
        userRepository.save(user);
        return MappingUtils.mapToUserModel(user);
    }

    @Override
    public User getCurrentUser() {
        return null;
    }
}
