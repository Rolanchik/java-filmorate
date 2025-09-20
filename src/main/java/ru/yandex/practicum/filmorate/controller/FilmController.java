package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);

        validateFilm(film);

        film.setId(nextId++);
        films.put(film.getId(), film);

        log.info("Фильм создан с ID: {}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);

        if (film.getId() == null) {
            log.error("ID фильма не указан для обновления");
            throw new ValidationException("ID фильма должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            log.error("Фильм с ID {} не найден", film.getId());
            throw new ValidationException("Фильм с ID " + film.getId() + " не найден");
        }

        validateFilm(film);

        films.put(film.getId(), film);

        log.info("Фильм с ID {} обновлен", film.getId());
        return film;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов. Количество: {}", films.size());
        return films.values();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        if (film.getReleaseDate() != null) {
            LocalDate cinemaStartDate = LocalDate.of(1895, 12, 28);
            if (film.getReleaseDate().isBefore(cinemaStartDate)) {
                throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
            }
        }

        if (film.getDuration() != null && film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
    }
}

