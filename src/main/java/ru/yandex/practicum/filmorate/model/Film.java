package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder
@Data
public class Film {
    private LocalDate releaseDate;
    private long duration;
    private Set<Long> likes;
    private int rate;
    private long id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    @NotEmpty
    private String description;

    public Set<Long> getLikes() {
        return Objects.requireNonNullElseGet(likes, HashSet::new);
    }
}
