package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@UtilityClass
public class UserMapper {
    public static UserDto modelToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User modelFromDto(UserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User modelFromNewUserDto(NewUserDto user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User updateUserFields(User user, UpdatedUserDto updatedUser) {
        user.setEmail(Optional.ofNullable(updatedUser.getEmail())
                .filter(email -> !email.isBlank()).orElse(user.getEmail()));
        user.setName(Optional.ofNullable(updatedUser.getName())
                .filter(name -> !name.isBlank()).orElse(user.getName()));

        return user;
    }
}
