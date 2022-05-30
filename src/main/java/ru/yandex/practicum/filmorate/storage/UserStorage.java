package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //сохраняет пользователя
    User save(User user);

    //обновляет пользователя
    void update(User user);

    //возвращает список всех пользователей
    List<User> getAll();

    //проверяет наличие пользователя
    boolean isUserExist(int id);

    //возвращает пользователя
    User getUser(int id);
}
