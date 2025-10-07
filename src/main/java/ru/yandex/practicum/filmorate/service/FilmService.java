package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public void addLike(Integer filmId, Integer userId) {

        log.info("Пользователь {} ставит лайк фильму {}", userId, filmId);
        validateIds(filmId, userId);
        Film film = getFilmById(filmId);
        boolean added = film.getLikes().add(userId);

        if (added) {
            filmStorage.updateFilm(film);
            log.info("Лайк добавлен. Фильм {} теперь имеет {} лайков",
                    filmId, film.getLikes());
        } else {
            log.info("Пользователь {} уже поставил лайк фильму {}", userId, filmId);
        }
    }

    public void removeLike(Integer filmId, Integer userId) {

        log.info("Пользователь {} убирает лайк у фильма {}", userId, filmId);
        validateIds(filmId, userId);
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);

        if (film == null) {
            throw new RuntimeException("Фильм с id " + filmId + " не найден");
        }
        if (user == null) {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }

        boolean removed = film.getLikes().remove(userId);

        if (removed) {
            filmStorage.updateFilm(film);
            log.info("Лайк удален. Фильм {} теперь имеет {} лайков",
                    filmId, film.getLikes());
        } else {
            log.info("Пользователь {} не ставил лайк фильму {}", userId, filmId);
        }
    }

    public List<Film> getPopularFilms(Integer count) {

        if (count == null) {
            count = 10;
        }

        log.info("Получение {} самых популярных фильмов", count);

        List<Film> popularFilms = getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());

        log.info("Найдено {} популярных фильмов", popularFilms.size());
        return popularFilms;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public Film createFilm(Film film) {
        log.info("Создание фильма: {}", film.getName());
        validateFilm(film);
        Film createdFilm = filmStorage.createFilm(film);
        log.info("Фильм создан с ID: {}", createdFilm.getId());
        return createdFilm;
    }

    public Film updateFilm(Film film) {
        if (getFilmById(film.getId()) == null) {
            throw new RuntimeException("Фильм с id " + film.getId() + " не найден");
        }

        validateFilm(film);
        getFilmById(film.getId());
        Film updatedFilm = filmStorage.updateFilm(film);
        log.info("Фильм с ID {} успешно обновлен", updatedFilm.getId());
        return updatedFilm;
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

    private void validateIds(Integer filmId, Integer userId) {

        if (filmId == null) {
            throw new ValidationException("ID фильма не может быть null");
        }

        if (userId == null) {
            throw new ValidationException("ID пользователя не может быть null");
        }
    }
}