package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private static final String SAME_EMAIL_ERROR = "Такая электронная почта уже используется";
    private static final String EMAIL_FORMAT_ERROR = "Электронная почта должна содержать символ @";
    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";

    @Override
    @Transactional
    public UserDto create(NewUserDto newUserDto) {
        if (!newUserDto.getEmail().contains("@")) {
            throw new ValidationException(EMAIL_FORMAT_ERROR);
        }

        User newUser = UserMapper.modelFromNewUserDto(newUserDto);
        return UserMapper.modelToDto(userRepository.save(newUser));
    }

    @Override
    @Transactional
    public UserDto update(int userId, UpdatedUserDto updatedUser) {
        validateUpdate(updatedUser);
        User oldUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));
        User newUser = UserMapper.updateUserFields(oldUser, updatedUser);
        return UserMapper.modelToDto(userRepository.save(newUser));
    }

    @Override
    public UserDto getUserById(int userId) {
        User foundUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));
        return UserMapper.modelToDto(foundUser);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::modelToDto)
                .toList();
    }

    public void validateUpdate(UpdatedUserDto user) {
        List<User> userWithSameEmail = userRepository.findAll().stream()
                .filter(u -> u.getEmail().equals(user.getEmail()))
                .toList();
        if (!userWithSameEmail.isEmpty()) {
            throw new ConflictException(SAME_EMAIL_ERROR);
        }
    }
}
