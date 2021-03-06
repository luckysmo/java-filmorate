package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class InMemoryFilmStorage implements FilmStorage {
    private static long idCounter = 0;
    private final Map<Long, Film> films = new HashMap<>();

    public boolean isFilmExist(long id) {
        return films.containsKey(id);
    }

    @Override
    public Film getFilm(long id) {
        return films.get(id);
    }

    private void setId(Film film) {
        idCounter++;
        film.setId(idCounter);
    }

    public Film save(Film film) {
        setId(film);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен.", film);
        return film;
    }

    public Film update(Film film) {
        films.replace(film.getId(), film);
        log.info("Фильм  c id {} обновлен.", film.getId());
        return film;
    }

    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
}
