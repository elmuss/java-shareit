package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class NewUserDto {
    private Integer id;
    private String name;
    @NonNull
    private String email;
}
