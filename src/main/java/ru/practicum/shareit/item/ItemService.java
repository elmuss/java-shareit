package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto newItem, int ownerId);

    ItemDto update(int itemId, ItemDto updatedItem, int ownerId);

    ItemDto getItemById(int itemId);

    void deleteItem(int itemId);

    Collection<ItemDto> findAll(int ownerId);

    List<ItemDto> searchItems(String text);
}
