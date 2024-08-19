package ru.practicum.shareit.item.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    int id;
    String name;
    String description;
    Boolean available;
    int ownerId;
    int requestId;
}
