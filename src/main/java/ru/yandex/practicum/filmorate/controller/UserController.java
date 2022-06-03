package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@Validated
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/users/{id}/friends/common/{otherId}")
    @ResponseBody
    public List<User> commonFriend(@PathVariable int id,
                                   @PathVariable int otherId) {
        return userService.getCommonFriend(id, otherId);
    }


    @GetMapping("/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id,
                          @PathVariable long friendId) {
        if (id < 0 || friendId < 0) {
            throw new NotFoundException("ID не может быть отрицательным!!");
        } else {
            userService.addFriend(id, friendId);
        }
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id,
                             @PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable long id) {
        if (id > 0) {
            return userService.getUserById(id);
        } else {
            throw new NotFoundException("ID не может быть отрицательным!!");
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    @PutMapping(value = "/users")
    public User patchUser(@Valid @RequestBody User user) {
        if (user.getId() > 0) {
            userService.patchUser(user);
            return user;
        } else {
            throw new NotFoundException("ID не может быть отрицательным!!");
        }
    }
}
