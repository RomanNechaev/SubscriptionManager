package ru.matmex.subscription.utils;


import ru.matmex.subscription.entities.User;

/**
 * Создание объекта пользователя
 */
public class UserBuilder {
    private String username;

    private String password;

    private String email;

    private UserBuilder() {
    }

    public static UserBuilder anUser() {
        return new UserBuilder();
    }

    public UserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public User build() {
        return new User(username, password, email);
    }

    public User defaultUser() {
        return UserBuilder.anUser()
                .withUsername("test")
                .withEmail("test@gmail.com")
                .withPassword("123")
                .build();
    }

}
