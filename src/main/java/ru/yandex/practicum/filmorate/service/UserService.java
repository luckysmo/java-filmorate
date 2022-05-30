package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public User getUserById(int id) {
        return userStorage.getUser(id);
    }

    public User addUser(@Valid User user) throws ValidationException {
        if (!userStorage.isUserExist(user.getId())) {
            validationCheck(user);
            return userStorage.save(user);
        } else {
            log.error("Пользователь с ID {} уже зарегестрирован!!!", user.getId());
            throw new ValidationException("Пользователь с таким ID уже зарегестрирован!!!");
        }
    }

    public void patchUser(@Valid User user) throws ValidationException {
        try {
            validationCheck(user);
            int id = user.getId();
            if (userStorage.isUserExist(id)) {
                userStorage.update(user);
            } else {
                log.error("Нет такого пользователя {}!", user);
                throw new NotFoundException("Нет такого пользователя!");
            }
        } catch (ValidationException exception) {
            log.error("Ошибка валидации обновления пользователя!!!");
            throw new ValidationException("Ошибка валидации обновления пользователя!!!");
        }
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    private void validationCheck(@Valid User user) throws ValidationException {
        if (!StringUtils.containsWhitespace(user.getLogin())) {
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            if (user.getBirthday().isAfter(LocalDate.now())) {
                log.error("Дата рождения не может быть позже, чем {}!!!", LocalDate.now());
                throw new ValidationException("Дата рождения не может быть позже, чем сегодня!!!");
            }
        } else {
            log.error("В логине не может быть пробелов!!!");
            throw new ValidationException("В логине не может быть пробелов!!!");
        }
    }

    public void addFriend(int id, int friendId) {
        Set<Integer> ids = checkFriends(id);
        ids.add(friendId);
        userStorage.getUser(id).setFriendsIds(ids);

        Set<Integer> friendIds = checkFriends(friendId);
        friendIds.add(id);
        userStorage.getUser(friendId).setFriendsIds(friendIds);
        log.info("Пользователь {} добавил в друзья пользователя {}",
                userStorage.getUser(id),
                userStorage.getUser(friendId));
    }

    private Set<Integer> checkFriends(int id) {
        Set<Integer> friendIds;
        if (userStorage.getUser(id).getFriendsIds() != null) {
            friendIds = userStorage.getUser(id).getFriendsIds();
        } else {
            friendIds = new HashSet<>();
        }
        return friendIds;
    }

    public void deleteFriend(int id, int friendId) {
        userStorage.getUser(id).getFriendsIds().remove(friendId);
        userStorage.getUser(friendId).getFriendsIds().remove(id);
        log.info("Пользователь {} удалил из друзей пользователя {}",
                userStorage.getUser(id),
                userStorage.getUser(friendId));
    }

    public List<User> getAllFriends(int id) {
        User user = userStorage.getUser(id);
        List<User> friends = new ArrayList<>();
        if (user.getFriendsIds() != null) {
            for (Integer friendId : user.getFriendsIds()) {
                friends.add(userStorage.getUser(friendId));
            }
            log.info("Пользователь {} запросил список всех своих друзей", userStorage.getUser(id));
            return friends;
        } else {
            log.info("Список друзей пользователя {} пуст!", userStorage.getUser(id));
            return new ArrayList<>();
        }
    }

    public List<User> getCommonFriend(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>(getAllFriends(id));
        commonFriends.retainAll(getAllFriends(otherId));
        log.info("Пользователь {} запросил список общих друзей с пользователем {}",
                userStorage.getUser(id),
                userStorage.getUser(otherId));
        return commonFriends;
    }
}
