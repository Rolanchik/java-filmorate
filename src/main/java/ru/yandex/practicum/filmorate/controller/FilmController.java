package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        Film createdFilm = filmService.createFilm(film);
        log.info("Фильм создан с ID: {}", film.getId());
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("Фильм с ID {} обновлен", updatedFilm.getId());
        return updatedFilm;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен запрос на получение всех фильмов");
        Collection<Film> films = filmService.getAllFilms();
        log.info("Возвращено фильмов: {}", films.size());
        return films;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Получен запрос на получение фильма с ID: {}", id);
        Film film = filmService.getFilmById(id);
        log.info("Фильм с ID {} найден", id);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пользователь {} добавляет лайк фильму {}", userId, id);
        filmService.addLike(id, userId);
        log.info("Лайк добавлен фильму с ID {}", id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Пользователь {} убирает лайк с фильма {}", userId, id);
        filmService.removeLike(id, userId);
        log.info("Лайк убран с фильма с ID {}", id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Получен запрос на получение {} популярных фильмов", count);
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.info("Возвращено популярных фильмов: {}", popularFilms.size());
        return popularFilms;
    }
}