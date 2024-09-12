package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserRepository userRepository;

    private User user;
    private UserDto userDto;
    private NewUserDto newUserDto;
    private UpdatedUserDto updatedUserDto;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        userService = new UserServiceImpl(userRepository);
        newUserDto = NewUserDto.builder().id(1).name("name").email("email@email").build();
        updatedUserDto = UpdatedUserDto.builder().name("newName").build();
        user = UserMapper.modelFromNewUserDto(newUserDto);
        userDto = UserMapper.modelToDto(user);
    }

    @Test
    void create() {
        UserDto result = userService.create(newUserDto);
        Assertions.assertNotNull(result);
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void getAll() {
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userRepository.findAll()).thenReturn(users);
        Collection<UserDto> result = userService.findAll();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        UserDto result = userService.getUserById(user.getId());
        Assertions.assertNotNull(result);
        verify(userRepository, times(1)).findById(user.getId());
    }
}