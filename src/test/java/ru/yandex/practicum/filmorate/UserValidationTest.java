/*package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    private UserController userController;
    private User validUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        validUser = new User();
        validUser.setEmail("test@example.com");
        validUser.setLogin("testuser");
        validUser.setName("Test User");
        validUser.setBirthday(LocalDate.of(1990, 1, 1));
    }

    @Test
    @DisplayName("Валидация должна провалиться при null email")
    void validateUser_ShouldThrowException_WhenEmailIsNull() {
        validUser.setEmail(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при пустом email")
    void validateUser_ShouldThrowException_WhenEmailIsEmpty() {
        validUser.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при email из пробелов")
    void validateUser_ShouldThrowException_WhenEmailIsOnlySpaces() {
        validUser.setEmail("   ");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Электронная почта не может быть пустой", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при email без @")
    void validateUser_ShouldThrowException_WhenEmailWithoutAt() {
        validUser.setEmail("testexample.com");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Электронная почта должна содержать символ @", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при null login")
    void validateUser_ShouldThrowException_WhenLoginIsNull() {
        validUser.setLogin(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при пустом login")
    void validateUser_ShouldThrowException_WhenLoginIsEmpty() {
        validUser.setLogin("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при login из пробелов")
    void validateUser_ShouldThrowException_WhenLoginIsOnlySpaces() {
        validUser.setLogin("   ");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Логин не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при login с пробелами")
    void validateUser_ShouldThrowException_WhenLoginContainsSpaces() {
        validUser.setLogin("test user");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Логин не может содержать пробелы", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при null дате рождения")
    void validateUser_ShouldPass_WhenBirthdayIsNull() {
        validUser.setBirthday(null);

        assertDoesNotThrow(() -> userController.createUser(validUser));
    }

    @Test
    @DisplayName("Валидация должна провалиться при будущей дате рождения")
    void validateUser_ShouldThrowException_WhenBirthdayInFuture() {
        validUser.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(validUser));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при сегодняшней дате рождения")
    void validateUser_ShouldPass_WhenBirthdayIsToday() {
        validUser.setBirthday(LocalDate.now());

        assertDoesNotThrow(() -> userController.createUser(validUser));
    }

    @Test
    @DisplayName("Имя должно заменяться логином при null")
    void createUser_ShouldSetLoginAsName_WhenNameIsNull() {
        validUser.setName(null);

        User result = userController.createUser(validUser);
        assertEquals("testuser", result.getName());
    }

    @Test
    @DisplayName("Имя должно заменяться логином при пустой строке")
    void createUser_ShouldSetLoginAsName_WhenNameIsEmpty() {
        validUser.setName("");

        User result = userController.createUser(validUser);
        assertEquals("testuser", result.getName());
    }

    @Test
    @DisplayName("Имя должно заменяться логином при строке из пробелов")
    void createUser_ShouldSetLoginAsName_WhenNameIsOnlySpaces() {
        validUser.setName("   ");

        User result = userController.createUser(validUser);
        assertEquals("testuser", result.getName());
    }
}
