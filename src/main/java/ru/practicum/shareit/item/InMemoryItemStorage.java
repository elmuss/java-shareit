package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
@Getter
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();

    private static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";

    @Override
    public Item create(Item newItem, int ownerId) {
        newItem.setId(getNextId());
        newItem.setOwnerId(ownerId);
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    private int getNextId() {
        int currentMaxId = items.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Item update(int itemId, Item updatedItem) {
        items.put(itemId, updatedItem);
        return updatedItem;
    }

    @Override
    public Item findItemById(int id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        } else {
            return items.get(id);
        }
    }

    @Override
    public Collection<Item> findAll(int ownerId) {
        return items.values().stream().filter(item -> item.getOwnerId() == ownerId).toList();
    }

    @Override
    public void deleteItem(int id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        }
        items.remove(id);
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> (item.getName().toUpperCase().contains(text.toUpperCase())
                        || item.getDescription().toUpperCase().contains(text.toUpperCase()))
                        && item.getAvailable())
                .toList();
    }
}
