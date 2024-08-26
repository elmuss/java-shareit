package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage {
    Item create(Item newItem, int ownerId);

    Item update(int itemId, ItemDto updatedItem);

    Item findItemById(int id);

    Collection<Item> findAll(int ownerId);

    void deleteItem(int id);

    List<Item> searchItems(String text);
}
