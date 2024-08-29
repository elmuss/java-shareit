package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User newUser);

    User update(int id, User updatedUser);

    User findUserById(int id);

    Collection<User> findAll();

    void deleteUser(int id);
}
