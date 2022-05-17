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
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.time.Month.DECEMBER;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    ObjectMapper mapper;
    @Autowired
    FilmService service;
    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldNotAddFilmTestWithEmptyDescription() throws Exception {
        Film film = Film.builder()
                .name("test")
                .description("")
                .releaseDate(LocalDate.of(2020, DECEMBER, 23))
                .duration(Duration.ofHours(2))
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotAddFilmTestWithDescriptionMoreThan200Chars() throws Exception {
        Film film = Film.builder()
                .name("test")
                .description("ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" +
                        "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(Duration.ofHours(2))
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    void shouldAddFilm() throws ValidationException {
        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(Duration.ofHours(2))
                .build();
        assertNotNull(service.addFilm(film));
    }

    @Test
    void shouldNotAddFilmTestWithEmptyName() throws Exception {
        Film film = Film.builder()
                .name("")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(Duration.ofHours(2))
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotAddFilmTestWithReleaseBeforeFirstFilmRelease() {
        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(1890, Month.MAY, 23))
                .duration(Duration.ofHours(2))
                .build();
        assertThrows(ValidationException.class, () -> service.addFilm(film));
    }

    @Test
    void shouldGetAllFilms() {
        Film film = Film.builder()
                .name("test")
                .description("XXX")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER, 23))
                .duration(Duration.ofHours(2))
                .id(1)
                .build();

        List<Film> expected = new ArrayList<>();
        expected.add(film);
        List<Film> actual = service.getAllFilms();

        assertArrayEquals(expected.toArray(), actual.toArray());
    }

}
