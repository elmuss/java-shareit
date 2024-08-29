package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(NewItemDto newItem, int ownerId);

    ItemDto update(int itemId, UpdatedItemDto updatedItem, int ownerId);

    ItemDto getItemById(int itemId);

    void deleteItem(int itemId);

    Collection<ItemDto> findAll(int ownerId);

    List<ItemDto> searchItems(String text);
}
