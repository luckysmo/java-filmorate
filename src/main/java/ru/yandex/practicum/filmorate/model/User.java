package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
public class User {
    private long id;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friendsIds;
    private Set<Long> likes;
}
