package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Slf4j
@Repository
@Primary
@RequiredArgsConstructor
public class InMemoryItemStorage implements ItemStorage {
    private final Map<Integer, Item> items = new HashMap<>();

    static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    static final String NULL_NAME_ERROR = "Поле с наименованием должно быть заполнено";
    static final String NULL_DESCRIPTION_ERROR = "Поле с описанием должно быть заполнено";
    static final String NULL_AVAILABLE_ERROR = "Необходимо указать, доступна ли вещь для аренды";
    static final String WRONG_OWNER_ID_ERROR = "Обновление доступно только владельцу";

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
    public Item update(Item updatedItem, int ownerId) {
        if (!items.containsKey(updatedItem.getId())) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        }

        if (items.get(updatedItem.getId()).getOwnerId() != ownerId) {
            throw new NotFoundException(WRONG_OWNER_ID_ERROR);
        }
        items.put(updatedItem.getId(), updatedItem);
        return updatedItem;
    }

    @Override
    public Optional<Item> findItemById(int id) {
        if (!items.containsKey(id)) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        } else {
            return Optional.of(items.get(id));
        }
    }

    @Override
    public Collection<Item> findAll(int ownerId) {
        return items.values().stream().filter(item -> item.getOwnerId()== ownerId).toList();
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

        if (item.getAvailable() == null) {
            throw new ValidationException(NULL_AVAILABLE_ERROR);
        }
    }
}
