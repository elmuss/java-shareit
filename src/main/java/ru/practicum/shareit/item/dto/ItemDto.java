package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    int requestId;
}
