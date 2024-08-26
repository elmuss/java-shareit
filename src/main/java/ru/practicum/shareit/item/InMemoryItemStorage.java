package ru.practicum.shareit.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
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
    private static final String NULL_NAME_ERROR = "Наименование не может быть пустым";
    private static final String NULL_DESCRIPTION_ERROR = "Описание не может быть пустым";

    @Override
    public Item create(Item newItem, int ownerId) {
        validate(newItem);
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
    public Item update(int itemId, ItemDto updatedItemDto) {
        if (!items.containsKey(itemId)) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        }

        updatedItemDto.setId(itemId);
        Item oldItem = items.get(itemId);

        oldItem.setName(Optional.ofNullable(updatedItemDto.getName())
                .filter(name -> !name.isBlank()).orElse(oldItem.getName()));
        oldItem.setDescription(Optional.ofNullable(updatedItemDto.getDescription())
                .filter(description -> !description.isBlank()).orElse(oldItem.getDescription()));
        if (updatedItemDto.getAvailable() != null) {
            oldItem.setAvailable(updatedItemDto.getAvailable());
        }
        return oldItem;
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

    public void validate(Item item) {
        if (item.getName().isBlank()) {
            throw new ValidationException(NULL_NAME_ERROR);
        }
        if (item.getDescription().isBlank()) {
            throw new ValidationException(NULL_DESCRIPTION_ERROR);
        }
    }
}
