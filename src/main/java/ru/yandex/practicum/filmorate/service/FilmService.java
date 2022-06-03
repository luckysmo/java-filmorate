package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    UserStorage userStorage;
    @Autowired
    private FilmStorage filmStorage;

    public Optional<Film> addFilm(@Valid Film film) throws ValidationException {
        validationCheck(film);
        return Optional.ofNullable(Optional.ofNullable(filmStorage.save(film))
                .orElseThrow(() -> new ValidationException("Фильм с таким ID уже зарегестрирован!!!")));
    }

    public Optional<Film> patchFilm(@Valid Film film) throws ValidationException {
        if (filmStorage.isFilmExist(film.getId())) {
            return Optional.ofNullable(filmStorage.update(film));
        } else {
            validationCheck(film);
            return Optional.ofNullable(filmStorage.save(film));
        }
    }

    private void validationCheck(@Valid Film film) throws ValidationException {
        LocalDate firstFilmDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getId() < 0) {
            throw new NotFoundException("ID не может быть отрицательным");
        }
        if (film.getReleaseDate().isAfter(firstFilmDate)) {
            if (film.getDuration() < 0) {
                log.error("Продолжительность фильма не может быть отрицательной!!!");
                throw new ValidationException("Продолжительность фильма не может быть отрицательной!!!");
            }
        } else {
            log.error("Фильм не может быть снят раньше {}!", firstFilmDate);
            throw new ValidationException("Ошибка валидации даты выхода фильма");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public void putLike(long filmId, long userId) {
        Set<Long> filmLikes = checkLikeFilm(filmId);
        filmLikes.add(userId);
        filmStorage.getFilm(filmId).setLikes(filmLikes);

        Set<Long> userLikes = checkLikeUser(userId);
        userLikes.add(filmId);
        userStorage.getUser(userId).setLikes(userLikes);
        log.info("Пользователь {} поставил лайк фильму {}",
                userStorage.getUser(userId),
                filmStorage.getFilm(filmId));
    }

    private Set<Long> checkLikeFilm(long id) {
        Set<Long> likesIds;
        if (filmStorage.getFilm(id).getLikes() != null) {
            likesIds = filmStorage.getFilm(id).getLikes();
        } else {
            likesIds = new HashSet<>();
        }
        return likesIds;
    }

    private Set<Long> checkLikeUser(long id) {
        Set<Long> likesIds;
        if (userStorage.getUser(id).getLikes() != null) {
            likesIds = userStorage.getUser(id).getLikes();
        } else {
            likesIds = new HashSet<>();
        }
        return likesIds;
    }

    public void deleteLike(long filmId, long userId) {
        if (userId < 0 || filmId < 0) {
            throw new NotFoundException("ID не может быть отрицательным");
        } else {
            if (filmStorage.getFilm(filmId).getLikes().contains(userId)) {
                filmStorage.getFilm(filmId).getLikes().remove(userId);
                userStorage.getUser(userId).getLikes().remove(filmId);
                log.info("Пользователь {} удалил лайк фильма {}",
                        userStorage.getUser(userId),
                        filmStorage.getFilm(filmId));
            } else {
                throw new NotFoundException("Нет такого лайка");
            }
        }
    }

    public List<Film> getPopular(Integer count) {
        List<Film> result = filmStorage.getAll().stream()
                .filter(film -> film.getLikes() != null)
                .sorted(Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
        log.info("Список популярных фильмов {}", result);
        return result;
    }

    public Film getFilm(long id) {
        if (id >= 0) {
            return filmStorage.getFilm(id);
        } else {
            throw new NotFoundException("ID не может быть отрицательным");
        }
    }
}