package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepo;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User addUser(@Valid User user) throws ValidationException {
        if (!userRepo.isUserExist(user.getId())) {
            validationCheck(user);
            return userRepo.save(user);
        } else {
            log.error("Пользователь с ID {} уже зарегестрирован!!!", user.getId());
            throw new ValidationException("Пользователь с таким ID уже зарегестрирован!!!");
        }
    }

    public void patchUSer(@Valid User user) throws ValidationException {
        try {
            validationCheck(user);
            int id = user.getId();
            if (userRepo.isUserExist(id)) {
                userRepo.update(user);
            } else {
                userRepo.save(user);
            }
        } catch (ValidationException exception) {
            log.error("Ошибка валидации обновления фильма!!!");
            throw new ValidationException("Ошибка валидации обновления фильма!!!");
        }
    }

    public List<User> getAllUsers() {
        return userRepo.getAll();
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
}
