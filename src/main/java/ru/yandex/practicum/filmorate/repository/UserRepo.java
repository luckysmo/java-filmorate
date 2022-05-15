package ru.yandex.practicum.filmorate.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserRepo {
    private static int idCounter = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public boolean isUserExist(int id) {
        return users.containsKey(id);
    }

    private void setId(User user) {
        idCounter++;
        user.setId(idCounter);
    }

    public User save(User user) {
        setId(user);
        users.put(user.getId(), user);
        log.info("Фильм {} добавлен.", user);
        return user;
    }

    public void update(User user) {
        users.replace(user.getId(), user);
        log.info("Фильм  c id {} обновлен.", user.getId());
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
