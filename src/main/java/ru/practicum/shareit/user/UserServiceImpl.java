package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    private static final String SAME_EMAIL_ERROR = "Такая электронная почта уже используется";

    @Override
    public UserDto create(NewUserDto newUserDto) {
        User newUser = UserMapper.modelFromNewUserDto(newUserDto);
        return UserMapper.modelToDto(userStorage.create(newUser));
    }

    @Override
    public UserDto update(int userId, UpdatedUserDto updatedUser) {
        validateUpdate(updatedUser);
        User oldUser = userStorage.findUserById(userId);
        User newUser = UserMapper.updateUserFields(oldUser, updatedUser);
        return UserMapper.modelToDto(userStorage.update(userId, newUser));
    }

    @Override
    public UserDto getUserById(int userId) {
        User foundUser = userStorage.findUserById(userId);
        return UserMapper.modelToDto(foundUser);
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

    public void validateUpdate(UpdatedUserDto user) {
        List<User> userWithSameEmail = userStorage.findAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .toList();
        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException(SAME_EMAIL_ERROR);
        }
    }
}
