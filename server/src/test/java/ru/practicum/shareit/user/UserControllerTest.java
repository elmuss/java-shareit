package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {
    @MockBean
    private UserService userService;

    private final ApplicationContext applicationContext;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @Test
    void createUser() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder().id(1).name("name").email("email@email").build();
        UserDto userDto = UserDto.builder().id(1).name("name").email("email@email").build();

        when(userService.create(newUserDto)).thenReturn(userDto);

        mockMvc
                .perform(
                        post("/users")
                                .content(mapper.writeValueAsString(newUserDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newUserDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(newUserDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(newUserDto.getEmail()), String.class));
        verify(userService, times(1)).create(newUserDto);
    }

    @Test
    void updateUser() throws Exception {
        UserDto userDto = UserDto.builder().id(1).name("newName").email("email@email").build();
        UpdatedUserDto updatedUserDto = UpdatedUserDto.builder().id(1).name("newName").email("email@email").build();

        when(userService.update(1, updatedUserDto)).thenReturn(userDto);

        mockMvc
                .perform(
                        patch("/users/{id}", 1)
                                .content(mapper.writeValueAsString(updatedUserDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
        verify(userService, times(1)).update(1, updatedUserDto);
    }

    @Test
    void getUserById() throws Exception {
        NewUserDto newUserDto = NewUserDto.builder().id(1).name("name").email("email@email").build();
        User user = UserMapper.modelFromNewUserDto(newUserDto);
        UserDto userDto = UserMapper.modelToDto(user);

        when(userService.getUserById(1)).thenReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"name\",\n" +
                        "    \"email\": \"email@email\"\n" +
                        "}"));
        verify(userService, times(1)).getUserById(1);
    }

    @Test
    void findAllUsers() throws Exception {
        when(userService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(userService, times(1)).findAll();
    }
}