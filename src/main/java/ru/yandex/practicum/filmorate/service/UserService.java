package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public void addFriend(Integer userId, Integer friendId) {
        validateUsersExist(userId, friendId);
        log.info("Добавление в друзья: пользователь {} и пользователь {}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        validateUsersExist(userId, friendId);
        log.info("Удаление из друзей: пользователь {} и пользователь {}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        if (user == null) {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }

        if (friend == null) {
            throw new RuntimeException("Пользователь с id " + friendId + " не найден");
        }

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        userStorage.updateUser(user);
        userStorage.updateUser(friend);
        log.info("Пользователи {} и {} больше не друзья", userId, friendId);
    }

    public List<User> getFriends(Integer userId) {

        log.info("Получение списка друзей пользователя {}", userId);
        User user = getUserById(userId);

        if (user == null) {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }

        return user.getFriends().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer userId, Integer otherUserId) {
        log.info("Поиск общих друзей пользователей {} и {}", userId, otherUserId);
        validateUsersExist(userId, otherUserId);
        User user = getUserById(userId);
        User otherUser = getUserById(otherUserId);

        if (user == null) {
            throw new RuntimeException("Пользователь с id " + userId + " не найден");
        }

        if (otherUser == null) {
            throw new RuntimeException("Пользователь с id " + otherUserId + " не найден");
        }

        Set<Integer> commonFriendsIds = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        List<User> commonFriends = commonFriendsIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());

        log.info("Найдено {} общих друзей", commonFriends.size());
        return commonFriends;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Integer id) {
        return userStorage.getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    public User createUser(User user) {
        validateUser(user);

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {

        if (getUserById(user.getId()) == null) {
            throw new RuntimeException("Пользователь с id " + user.getId() + " не найден");
        }

        getUserById(user.getId());
        validateUser(user);

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            user.setName(user.getLogin());
        }

        return userStorage.updateUser(user);
    }

    private void validateUsersExist(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);
    }

    private void validateUser(User user) {

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Электронная почта не может быть пустой");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта должна содержать символ @");
        }

        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может содержать пробелы");
        }

        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
