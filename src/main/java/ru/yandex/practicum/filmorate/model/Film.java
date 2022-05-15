package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Builder
@Data
public class Film {
    int id;
    @NotEmpty
    String name;
    @Size(max = 200)
    @NotEmpty
    String description;
    LocalDate releaseDate;
    Duration duration;
}