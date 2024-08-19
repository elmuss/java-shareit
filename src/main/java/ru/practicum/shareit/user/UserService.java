package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto create(User newUser);

    UserDto update(int userId, User updatedUser);

    UserDto getUserById(int userId);

    void deleteUser(int userId);

    Collection<UserDto> findAll();
}
