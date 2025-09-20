package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidationTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        validFilm = new Film();
        validFilm.setName("Test Film");
        validFilm.setDescription("Test Description");
        validFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        validFilm.setDuration(120);
    }

    @Test
    @DisplayName("Валидация должна провалиться при null названии")
    void validateFilm_ShouldThrowException_WhenNameIsNull() {
        validFilm.setName(null);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при пустом названии")
    void validateFilm_ShouldThrowException_WhenNameIsEmpty() {
        validFilm.setName("");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при названии из пробелов")
    void validateFilm_ShouldThrowException_WhenNameIsOnlySpaces() {
        validFilm.setName("   ");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при null описании")
    void validateFilm_ShouldPass_WhenDescriptionIsNull() {
        validFilm.setDescription(null);

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }

    @Test
    @DisplayName("Валидация должна пройти при описании ровно 200 символов")
    void validateFilm_ShouldPass_WhenDescriptionIs200Characters() {
        validFilm.setDescription("a".repeat(200));

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }

    @Test
    @DisplayName("Валидация должна провалиться при описании 201 символ")
    void validateFilm_ShouldThrowException_WhenDescriptionIs201Characters() {
        validFilm.setDescription("a".repeat(201));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Максимальная длина описания — 200 символов", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при null дате релиза")
    void validateFilm_ShouldPass_WhenReleaseDateIsNull() {
        validFilm.setReleaseDate(null);

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }

    @Test
    @DisplayName("Валидация должна провалиться при дате до 28 декабря 1895")
    void validateFilm_ShouldThrowException_WhenReleaseDateBeforeCinemaStart() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 27));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при дате 28 декабря 1895")
    void validateFilm_ShouldPass_WhenReleaseDateIsExactlyCinemaStart() {
        validFilm.setReleaseDate(LocalDate.of(1895, 12, 28));

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }

    @Test
    @DisplayName("Валидация должна пройти при null продолжительности")
    void validateFilm_ShouldPass_WhenDurationIsNull() {
        validFilm.setDuration(null);

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }

    @Test
    @DisplayName("Валидация должна провалиться при нулевой продолжительности")
    void validateFilm_ShouldThrowException_WhenDurationIsZero() {
        validFilm.setDuration(0);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна провалиться при отрицательной продолжительности")
    void validateFilm_ShouldThrowException_WhenDurationIsNegative() {
        validFilm.setDuration(-1);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(validFilm));
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    @DisplayName("Валидация должна пройти при продолжительности 1 минута")
    void validateFilm_ShouldPass_WhenDurationIsOne() {
        validFilm.setDuration(1);

        assertDoesNotThrow(() -> filmController.createFilm(validFilm));
    }
}

