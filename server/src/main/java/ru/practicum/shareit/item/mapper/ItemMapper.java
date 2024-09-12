package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@UtilityClass
public final class ItemMapper {
    public static ItemDto modelToDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getUser().getId())
                .build();
    }

    public static ItemBookingDto modelToBookingDto(Item item) {
        return ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getUser().getId())
                .build();
    }

    public static Item modelFromDto(ItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item modelFromNewItemDto(NewItemDto item) {
        return Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item updateItemFields(Item item, UpdatedItemDto updatedItem) {
        item.setName(Optional.ofNullable(updatedItem.getName())
                .filter(name -> !name.isBlank()).orElse(item.getName()));
        item.setDescription(Optional.ofNullable(updatedItem.getDescription())
                .filter(description -> !description.isBlank()).orElse(item.getDescription()));
        if (updatedItem.getAvailable() != null) {
            item.setAvailable(updatedItem.getAvailable());
        }
        return item;
    }
}
