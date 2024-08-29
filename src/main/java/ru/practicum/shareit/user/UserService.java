package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(NewUserDto newUser);

    UserDto update(int userId, UpdatedUserDto updatedUser);

    UserDto getUserById(int userId);

    void deleteUser(int userId);

    Collection<UserDto> findAll();
}
