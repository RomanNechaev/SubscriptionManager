package ru.matmex.subscription.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.repositories.UserRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.mapping.UserModelMapper;

import java.util.List;
import java.util.Optional;


@ContextConfiguration(classes = {UserServiceImpl.class, PasswordEncoder.class})
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserModelMapper userModelMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, userModelMapper, categoryService);
    }

    @Test
    void testCanLoadUserByUsername() {

    }

    @Test
    void testCanAddUser() {
        UserRegistrationModel userFromContext = new UserRegistrationModel("Test", "test@gmail.com", "123456");

        userService.adduser(userFromContext);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(2)).save(userArgumentCaptor.capture());

        User userCaptured = userArgumentCaptor.getValue();

        assertThat(userCaptured.getUsername()).isSameAs(userFromContext.username());

    }

    @Test
    void testWillThrowWhenUsernameIsTaken() {
        UserRegistrationModel userFromContext = new UserRegistrationModel("Test", "test@gmail.com", "123456");

        given(userRepository.existsByUsername(userFromContext.username())).willReturn(true);

        assertThatThrownBy(() -> userService.adduser(userFromContext))
                .isInstanceOf(AuthenticationServiceException.class)
                .hasMessageContaining("Пользователь с таким именем уже существует");

    }

    @Test
    void updateUsername() {
    }

    @Test
    void getUser() {
    }

    @Test
    void testGetCurrentUser() {
        SecurityContext fakeSecurityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        User user = new User("test", "test@gmail.com", "123");

        Mockito.when(authentication.getName()).thenReturn("test");

        when(fakeSecurityContext.getAuthentication()).thenReturn(authentication);

        User currentUser = userService.getCurrentUser();

        assertThat("test").isSameAs(currentUser.getUsername());
    }

    @Test
    void testCanDeleteUser() {
        String username = "test";
        User user = new User(username, "test@gmail.com", "123");
        given(userRepository.existsByUsername(username)).willReturn(true);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        userService.delete(username);

        verify(userRepository).delete(user);
    }

    @Test
    void testWillThrowWhenDeleteUserNotFound() {
        String username = "test";
        given(userRepository.existsByUsername(username)).willReturn(false);

        assertThatThrownBy(() -> userService.delete(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with" + username + " not found");
        verify(userRepository, never()).delete(any());

    }

    @Test
    void testCanGetAllUsers() {
        List<User> usersList = List.of(new User("test", "test@gmail.com", "123"),
                new User("test2", "test2@mail.ru", "123"));

        when(userRepository.findAll()).thenReturn(usersList);

        List<UserModel> allUsers = userService.getUsers();

        assertThat(usersList.size()).isEqualTo(allUsers.size());

        verify(userRepository).findAll();
    }

    @Test
    void createAdminIfNotExists() {
    }
}