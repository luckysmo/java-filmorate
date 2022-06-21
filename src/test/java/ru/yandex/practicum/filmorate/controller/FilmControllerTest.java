package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.DECEMBER;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FilmService filmService;
    @Autowired
    UserService userService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldNotAddFilmTestWithEmptyDescription() throws Exception {
        Film film = Film.builder()
                .name("test")
                .description("")
                .releaseDate(LocalDate.of(2020, DECEMBER, 23))
                .duration(180)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(500));
    }

    @Test
    void shouldNotAddFilmTestWithDescriptionMoreThan200Chars() throws Exception {
        Film film = Film.builder()
                .name("test")
                .description("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(180)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(500));
    }

    @Test
    void shouldAddFilm() throws ValidationException {
        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(180)
                .build();
        assertNotNull(filmService.addFilm(film));
    }

    @Test
    void shouldNotAddFilmTestWithEmptyName() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(180)
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(500));
    }

    @Test
    void shouldNotAddFilmTestWithReleaseBeforeFirstFilmRelease() {
        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(1890, Month.MAY, 23))
                .duration(180)
                .build();
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
    }

    @Test
    void shouldGetAllFilms() {
        List<Film> actual = filmService.getAllFilms();

        assertEquals(3, actual.size());
    }

    @Test
    void userPutsLikeTest() {
        User user = User.builder()
                .login("test")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(180)
                .build();

        userService.addUser(user);
        filmService.addFilm(film);
        filmService.putLike(user.getId(), film.getId());

        assertEquals(user.getLikes().size(), film.getLikes().size());
    }

    @Test
    void userDeleteLikeTest() {
        User user = User.builder()
                .login("test")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(180)
                .build();

        userService.addUser(user);
        filmService.addFilm(film);
        filmService.putLike(user.getId(), film.getId());
        filmService.deleteLike(film.getId(), user.getId());

        assertArrayEquals(user.getLikes().toArray(), new ArrayList<>().toArray());
    }
}
