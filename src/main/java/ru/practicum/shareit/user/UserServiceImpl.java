package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";

    @Override
    public UserDto create(UserDto newUserDto) {
        User newUser = UserMapper.modelFromDto(newUserDto);
        userStorage.create(newUser);
        return UserMapper.modelToDto(newUser);
    }

    @Override
    public UserDto update(int userId, UserDto updatedUserDto) {
        return UserMapper.modelToDto(userStorage.update(userId, updatedUserDto));
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
