package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        User createdUser = userService.createUser(user);
        log.info("Пользователь создан с ID: {}", createdUser.getId());
        return createdUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("Пользователь с ID {} обновлен", updatedUser.getId());
        return updatedUser;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        Collection<User> users = userService.getAllUsers();
        log.info("Возвращено пользователей: {}", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Получен запрос на получение пользователя с ID: {}", id);
        User user = userService.getUserById(id);
        log.info("Найден пользователь: {}", user.getLogin());
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос на добавление в друзья: пользователь {} добавляет {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Пользователь {} успешно добавил в друзья пользователя {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Получен запрос на удаление из друзей: пользователь {} удаляет {}", id, friendId);
        userService.removeFriend(id, friendId);
        log.info("Пользователь {} успешно удалил из друзей пользователя {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Integer id) {
        log.info("Получен запрос на получение друзей пользователя с ID: {}", id);
        Collection<User> friends = userService.getFriends(id);
        log.info("Найдено друзей: {} для пользователя {}", friends.size(), id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Получен запрос на получение общих друзей между пользователями {} и {}", id, otherId);
        Collection<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Найдено общих друзей: {} между пользователями {} и {}", commonFriends.size(), id, otherId);
        return commonFriends;
    }
}