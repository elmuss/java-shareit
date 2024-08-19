package ru.practicum.shareit.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    int id;
    String name;
    @NonNull
    String email;
}
