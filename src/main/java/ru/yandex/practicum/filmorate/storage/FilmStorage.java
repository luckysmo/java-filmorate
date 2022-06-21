package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    //сохраняет фильм
    Film save(Film film);

    //обновляет фильм
    Film update(Film film);

    //возвращает список всех фильмов
    List<Film> getAll();

    //проверяет наличие фильма
    boolean isFilmExist(long id);

    //возвращает фильм
    Film getFilm(long id);
}
