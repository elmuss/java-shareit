package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item create(Item newItem, int ownerId);

    Item update(Item updatedItem, int ownerId);

    Optional<Item> findItemById(int id);

    Collection<Item> findAll();

    void deleteItem(int id);

    List<Item> searchItems(String text);
}
