package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class Comment {
    private Integer id;
    private String text;
    private Integer itemId;
    private User author;
    private LocalDateTime created;
}
