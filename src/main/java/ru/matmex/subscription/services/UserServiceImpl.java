package ru.matmex.subscription.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.repositories.UserRepository;

@Component
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with name:" + username + "not found"));
    }

    @Override
    public void adduser(User user) {
        userRepository.findByUsername(user.getUsername()).orElse(userRepository.save(user));
    }

    @Override
    public void updateUsername(Long id,String newUsername) {
        User user = userRepository.getById(id).orElseThrow(()-> new UsernameNotFoundException("User not found"));
        user.setUsername(newUsername);
    }

    @Override
    public void updatePassword(String username) {
        //TODO
    }
}
