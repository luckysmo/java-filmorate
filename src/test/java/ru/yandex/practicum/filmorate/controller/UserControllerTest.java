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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @Autowired
    UserService service;

    @Test
    void shouldAddUser() {
        User user = User.builder()
                .login("test")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        assertNotNull(service.addUser(user));
    }

    @Test
    void shouldNotAddUserWithNotValidEmail() throws Exception {
        User user = User.builder()
                .login("test")
                .name("test")
                .email("XXXandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotAddUserWithNotValidLogin() {
        User user = User.builder()
                .login("te  st ")
                .name("xxx")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        assertThrows(ValidationException.class, () -> service.addUser(user)
        );
    }

    @Test
    void shouldNotAddUserWithEmptyLogin() throws Exception {
        User user = User.builder()
                .login("")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(400));
    }

    @Test
    void shouldNotAddUserWithNotValidBirthday() {
        User user = User.builder()
                .login("test")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(2023, Month.DECEMBER, 23))
                .build();

        assertThrows(ValidationException.class, () -> service.addUser(user));
    }

    @Test
    void shouldGetAllUsers() {
        User user = User.builder()
                .id(1)
                .login("test")
                .name("test")
                .email("XXX@yandex.ru")
                .birthday(LocalDate.of(1995, Month.DECEMBER, 23))
                .build();
        List<User> expected = new ArrayList<>();
        expected.add(user);
        List<User> actual = service.getAllUsers();

        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
