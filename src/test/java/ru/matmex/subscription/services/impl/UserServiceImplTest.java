package ru.matmex.subscription.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.repositories.UserRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.mapping.UserModelMapper;
import ru.matmex.subscription.utils.UserBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@ContextConfiguration(classes = {UserServiceImpl.class, PasswordEncoder.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserModelMapper userModelMapper;

    private UserService userService;

    private final User defaultUser = UserBuilder.anUser().defaultUser();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                passwordEncoder,
                userModelMapper,
                categoryService);
    }

    /**
     * Тестирование авторизации пользователя
     */
    @Test
    void testCanLoadUserByUsername() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(defaultUser));

        UserDetails user = new org.springframework.security.core.userdetails
                .User(defaultUser.getUsername(), defaultUser.getPassword(), defaultUser.getRoles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                .collect(Collectors.toList()));

        UserDetails userResult = userService.loadUserByUsername("test");
        assertThat(user).isEqualTo(userResult);
    }

    /**
     * Тестирование добавления пользователя
     */
    @Test
    void testCanAddUser() {
        UserRegistrationModel userFromContext = new UserRegistrationModel("Test", "test@gmail.com", "123456");

        userService.adduser(userFromContext);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(2)).save(userArgumentCaptor.capture());

        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getUsername()).isSameAs(userFromContext.username());
    }

    /**
     * Тестирование добавления пользователя, если никнейм уже занят
     */
    @Test
    void testWillThrowWhenUsernameIsTaken() {
        UserRegistrationModel userFromContext = new UserRegistrationModel("Test", "test@gmail.com", "123456");

        given(userRepository.existsByUsername(userFromContext.username())).willReturn(true);

        assertThatThrownBy(() -> userService.adduser(userFromContext))
                .isInstanceOf(AuthenticationServiceException.class)
                .hasMessageContaining("Пользователь с таким именем уже существует");
    }

    /**
     * Тестирование обновления пользователя
     */
    @Test
    void testCanUpdateUser() {
        String newEmail = "test@yandex.ru";
        UserUpdateModel userUpdateModel = new UserUpdateModel(12L, "test", newEmail);
        String oldEmail = "test@gmail.com";
        User user = new User("test", oldEmail, "123");

        when(userRepository.getById(12L)).thenReturn(Optional.of(user));

        userService.updateUser(userUpdateModel);

        String updatedEmail = user.getEmail();

        assertThat(updatedEmail).isSameAs("test@yandex.ru");
    }

    /**
     * Тестирование получения пользователя
     */
    @Test
    void canGetUser() {

        when(userRepository.findByUsername("test")).thenReturn(Optional.of(defaultUser));

        UserModel user = userService.getUser("test");

        assertThat(userModelMapper.build(defaultUser)).isEqualTo(user);
    }

    /**
     * Тестирование получения несуществующего пользователя
     */
    @Test
    void willThrownWhenGetUserReturnEmptyOptional() {
        String username = defaultUser.getUsername();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    /**
     * Тестирование получения текущего пользователя
     */
    @Test
    void testGetCurrentUser() {
    }

    /**
     * Тестирование удаления пользователя
     */
    @Test
    void testCanDeleteUser() {
        String username = defaultUser.getUsername();
        given(userRepository.existsByUsername(username)).willReturn(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(defaultUser));
        userService.delete(username);

        verify(userRepository).delete(defaultUser);
    }

    /**
     * Тестирование удаления несуществущего пользователя
     */
    @Test
    void testWillThrowWhenDeleteUserNotFound() {
        String username = defaultUser.getUsername();
        given(userRepository.existsByUsername(username)).willReturn(false);

        assertThatThrownBy(() -> userService.delete(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with" + username + " not found");
        verify(userRepository, never()).delete(any());

    }

    /**
     * Тестирование на получения всех пользователей
     */
    @Test
    void testCanGetAllUsers() {
        List<User> usersList = List
                .of(defaultUser,
                        UserBuilder.anUser()
                                .withUsername("test2")
                                .withEmail("test@gmail.com")
                                .withPassword("123")
                                .build());

        when(userRepository.findAll()).thenReturn(usersList);

        List<UserModel> allUsers = userService.getUsers();

        assertThat(usersList.size()).isEqualTo(allUsers.size());

        verify(userRepository).findAll();
    }
}