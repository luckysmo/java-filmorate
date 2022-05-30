package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class User {
    int id;
    @Email
    @NotEmpty
    String email;
    @NotEmpty
    String login;
    String name;
    LocalDate birthday;
    Set<Integer> friendsIds;
    Set<Integer> likes;
}
