package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepo;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    @Autowired
    private FilmRepo filmRepo;

    public Film addFilm(@Valid Film film) throws ValidationException {
        if (!filmRepo.isFilmExist(film.getId())) {
            validationCheck(film);
            return filmRepo.save(film);
        } else {
            log.error("Фильм с ID {} уже зарегестрирован!!!", film.getId());
            throw new ValidationException("Фильм с таким ID уже зарегестрирован!!!");
        }
    }

    public Film patchFilm(@Valid Film film) throws ValidationException {
        try {
            validationCheck(film);
            int id = film.getId();
            if (filmRepo.isFilmExist(id)) {
                return filmRepo.update(film);
            } else {
                return filmRepo.save(film);
            }
        } catch (ValidationException exception) {
            log.error("Ошибка валидации обновления фильма!!!");
            throw new ValidationException("Ошибка валидации обновления фильма!!!");
        }
    }

    private void validationCheck(@Valid Film film) throws ValidationException {
        LocalDate firstFilmDate = LocalDate.of(1895, Month.DECEMBER, 28);
        if (film.getReleaseDate().isAfter(firstFilmDate)) {
            if (film.getDuration().isNegative()) {
                log.error("Продолжительность фильма не может быть отрицательной!!!");
                throw new ValidationException("Продолжительность фильма не может быть отрицательной!!!");
            }
        } else {
            log.error("Фильм не может быть снят раньше {}!", firstFilmDate);
            throw new ValidationException("Ошибка валидации даты выхода фильма");
        }
    }

    public List<Film> getAllFilms() {
        return filmRepo.getAll();
    }
}
