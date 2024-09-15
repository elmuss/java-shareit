package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer ownerId;
    private Integer requestId;
}
