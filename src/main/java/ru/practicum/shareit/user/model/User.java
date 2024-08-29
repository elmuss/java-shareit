package ru.practicum.shareit.user.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    @NonNull
    private String email;
}
