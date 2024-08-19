package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(Item newItem, int ownerId);

    ItemDto update(int itemId, Item updatedItem, int ownerId);

    ItemDto getItemById(int itemId);

    void deleteItem(int itemId);

    Collection<ItemDto> findAll();

    List<Item> searchItems(String text);
}
