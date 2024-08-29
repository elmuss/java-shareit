package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Getter
@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String EMAIL_FORMAT_ERROR = "Электронная почта должна содержать символ @";
    private static final String SAME_EMAIL_ERROR = "Такая электронная почта уже используется";

    @Override
    public User create(User user) {
        validate(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User update(int userId, User updatedUser) {
        users.put(userId, updatedUser);
        return updatedUser;
    }

    @Override
    public User findUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        } else {
            return users.get(id);
        }
    }

    @Override
    public void deleteUser(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        }
        users.remove(id);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    public void validate(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException(EMAIL_FORMAT_ERROR);
        }

        List<User> userWithSameEmail = users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .toList();
        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException(SAME_EMAIL_ERROR);
        }
    }
}
