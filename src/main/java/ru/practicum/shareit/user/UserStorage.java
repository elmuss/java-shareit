package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User create(User newUser);

    User update(int id, User updatedUser);

    Optional<User> findUserById(int id);

    Collection<User> findAll();

    void deleteUser(int id);
}
