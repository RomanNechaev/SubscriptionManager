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
import ru.matmex.subscription.services.notifications.NotificationBroker;
import ru.matmex.subscription.services.utils.mapping.UserModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для операций с пользователем
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserModelMapper userModelMapper;
    private final CategoryService categoryService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserModelMapper userModelMapper,
                           @Lazy CategoryService categoryService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userModelMapper = userModelMapper;
        this.categoryService = categoryService;
        createAdmin();
    }

    /**
     * Загрузить пользователя по имени
     *
     * @param username - имя пользователя
     * @return авторизовачная информация о пользователе
     * @throws UsernameNotFoundException пользователь не найден
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name:" + username + "not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    /**
     * Добавить пользователя
     *
     * @param userRegistrationModel - регистрационные данные пользователя
     * @return информация о зарегистрированном пользователе
     */
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

    /**
     * Обновить пользователя
     *
     * @param userUpdateModel - обновленная информация о пользователе
     * @return информация об обновленном пользователе
     */
    @Override
    public UserModel updateUser(UserUpdateModel userUpdateModel) {
        User user = userRepository
                .getById(userUpdateModel.id())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setUsername(userUpdateModel.name());
        user.setEmail(userUpdateModel.email());
        userRepository.save(user);
        return userModelMapper.build(user);
    }

    /**
     * Получить пользователя по имени
     *
     * @param username - имя пользователя
     * @return информация о пользователе
     */
    @Override
    public UserModel getUserModel(String username) {
        return userModelMapper
                .build(userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Преобразовать роль к авторизационной роли spring-security
     *
     * @param roles список ролей
     * @return список авторизационных ролей
     */
    private List<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles
                .stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList());
    }

    /**
     * Получить пользователя в текущей сессии
     *
     * @return сущность пользователя
     */
    @Override
    public User getCurrentUser() {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        return userRepository
                .findByUsername(user.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")); //TODO
    }

    public void setTelegramChatId(String username, long telegramChatId) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setTelegramChatId(telegramChatId);
        userRepository.save(user);
    }

    /**
     * Удалить пользователя
     *
     * @param username имя пользователя
     * @return информация об удалении
     */
    @Override
    public String delete(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UsernameNotFoundException("User with" + username + " not found"); //TODO
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
        return "Пользователь успешно удален!";
    }

    /**
     * Получить всех пользователей
     *
     * @return список всех пользователей
     */
    @Override
    public List<UserModel> getUsers() {
        return userRepository
                .findAll()
                .stream().map(userModelMapper).toList();
    }

    @Override
    public boolean checkIntegrationWithTelegram() {
        return Optional.ofNullable(getCurrentUser().getTelegramChatId()).isPresent();
    }

    /**
     * Создать администратора
     */
    public void createAdmin() {
        User admin = new User("admin", "admin@mail.ru", passwordEncoder.encode("admin"), Role.ADMIN);
        admin.setRoles(Set.of(Role.ADMIN));

        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(admin);
        }
    }
}
