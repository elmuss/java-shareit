package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    final UserStorage userStorage;

    static final String USER_NOT_FOUND_MSG = "Пользователь не найден";

    @Override
    public UserDto create(User newUser) {
        userStorage.create(newUser);
        return UserMapper.modelToDto(newUser);
    }

    @Override
    public UserDto update(int userId, User updatedUser) {
        updatedUser.setId(userId);
        User oldUser = userStorage.findUserById(userId)
                .orElseThrow(() -> {
                    String idNotFound = String.format(USER_NOT_FOUND_MSG);
                    log.warn(USER_NOT_FOUND_MSG);
                    return new NotFoundException(idNotFound);
                });

        oldUser.setEmail(Optional.ofNullable(updatedUser.getEmail()).filter(email -> !email.isBlank()).orElse(oldUser.getEmail()));
        oldUser.setName(Optional.ofNullable(updatedUser.getName()).filter(name -> !name.isBlank()).orElse(oldUser.getName()));

        return UserMapper.modelToDto(userStorage.update(oldUser));
    }

    @Override
    public UserDto getUserById(int userId) {
        Optional<User> foundUser = userStorage.findUserById(userId);
        if (foundUser.isPresent()) {
            return UserMapper.modelToDto(foundUser.get());
        } else {
            throw new NotFoundException(String.format(USER_NOT_FOUND_MSG, userId));
        }
    }

    @Override
    public void deleteUser(int userId) {
        userStorage.deleteUser(userId);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::modelToDto)
                .toList();
    }
}
