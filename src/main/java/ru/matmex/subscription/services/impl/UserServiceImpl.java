package ru.matmex.subscription.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.Role;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.repositories.UserRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.mapping.UserModelMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserModelMapper userModelMapper;
    private CategoryService categoryService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserModelMapper userModelMapper,
                           @Lazy CategoryService categoryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userModelMapper = userModelMapper;
        this.categoryService = categoryService;
        createAdminIfNotExists();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name:" + username + "not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    @Override
    public UserModel adduser(UserRegistrationModel userRegistrationModel) {
        if (userRepository.existsByUsername(userRegistrationModel.username())) {
            throw new AuthenticationServiceException("Пользователь с таким именем уже существует");
        }
        User user = new User(userRegistrationModel.username(),
                userRegistrationModel.email(),
                passwordEncoder.encode(userRegistrationModel.password()));
        userRepository.save(user);
        categoryService.createDefaultSubscription(user);
        return userModelMapper.build(user);
    }

    @Override
    public UserModel updateUsername(UserUpdateModel userUpdateModel) {
        User user = userRepository
                .getById(userUpdateModel.id())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(userUpdateModel.name());
        user.setEmail(userUpdateModel.email());
        userRepository.save(user);
        return userModelMapper.build(user);
    }

    @Override
    public UserModel getUser(String username) {
        return userModelMapper.build(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));//TODO
    }

    private List<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());
    }

    @Override
    public User getCurrentUser() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        return userRepository
                .findByUsername(user.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); //TODO
    }

    @Override
    public String delete(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("User not found"); //TODO
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return "Пользователь успешно удален!";
    }

    @Override
    public List<UserModel> getUsers() {
        return userRepository
                .findAll()
                //TODO
                .stream().map(userModelMapper).toList();
    }

    public void createAdminIfNotExists() {
        User admin = new User("admin", "admin@mail.ru", passwordEncoder.encode("admin"), Role.ADMIN);
        admin.setRoles(Set.of(Role.ADMIN));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(admin);
        }
    }
}
