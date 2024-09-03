package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ItemBookingDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer ownerId;
    private LocalDateTime lastBooking;
    private LocalDateTime nextBooking;
    private List<Comment> comments;
}
