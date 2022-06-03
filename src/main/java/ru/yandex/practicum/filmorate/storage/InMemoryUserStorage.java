package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private static long idCounter = 0;
    private final Map<Long, User> users = new HashMap<>();

    public boolean isUserExist(long id) {
        return users.containsKey(id);
    }

    private void setId(User user) {
        idCounter++;
        user.setId(idCounter);
    }

    public User save(User user) {
        setId(user);
        users.put(user.getId(), user);
        log.info("Пользователь {} добавлен.", user);
        return user;
    }

    public void update(User user) {
        log.info("Пользователь  c id {} обновлен.", user.getId());
        users.replace(user.getId(), user);
    }

    public User getUser(long id) {
        return users.get(id);
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
