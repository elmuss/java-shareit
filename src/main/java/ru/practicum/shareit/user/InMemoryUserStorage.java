package ru.practicum.shareit.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
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
    private static final String NULL_EMAIL_ERROR = "Электронная почта не может быть пустой";
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
    public User update(int userId, UserDto updatedUser) {
        validateUpdate(updatedUser);
        if (!users.containsKey(userId)) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        }
        updatedUser.setId(userId);

        User oldUser = users.get(userId);
        oldUser.setEmail(Optional.ofNullable(updatedUser.getEmail())
                .filter(email -> !email.isBlank()).orElse(oldUser.getEmail()));
        oldUser.setName(Optional.ofNullable(updatedUser.getName())
                .filter(name -> !name.isBlank()).orElse(oldUser.getName()));

        users.put(userId, oldUser);
        return oldUser;
    }

    @Override
    public Optional<User> findUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        } else {
            return Optional.of(users.get(id));
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
        if (user.getEmail().isBlank()) {
            throw new ValidationException(NULL_EMAIL_ERROR);
        }

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

    public void validateUpdate(UserDto user) {
        List<User> userWithSameEmail = users.values().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .toList();
        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException(SAME_EMAIL_ERROR);
        }
    }
}
