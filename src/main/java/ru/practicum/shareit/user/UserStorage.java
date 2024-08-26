package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User newUser);

    User update(int id, UserDto updatedUser);

    Optional<User> findUserById(int id);

    Collection<User> findAll();

    void deleteUser(int id);
}
