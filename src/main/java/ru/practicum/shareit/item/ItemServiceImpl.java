package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    final ItemStorage itemStorage;
    final UserService userService;

    static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    static final String USER_NOT_FOUND_MSG = "Пользователь не найден";

    @Override
    public ItemDto create(Item newItem, int ownerId) {
        if (userService.getUserById(ownerId) == null) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        }
        itemStorage.create(newItem, ownerId);
        return ItemMapper.modelToDto(newItem);
    }

    @Override
    public ItemDto update(int itemId, Item updatedItem, int ownerId) {
        updatedItem.setId(itemId);
        Item oldItem = itemStorage.findItemById(itemId)
                .orElseThrow(() -> {
                    String idNotFound = String.format(ITEM_NOT_FOUND_MSG);
                    log.warn(ITEM_NOT_FOUND_MSG);
                    return new NotFoundException(idNotFound);
                });

        oldItem.setName(Optional.ofNullable(updatedItem.getName()).filter(name -> !name.isBlank()).orElse(oldItem.getName()));
        oldItem.setDescription(Optional.ofNullable(updatedItem.getDescription()).filter(description -> !description.isBlank()).orElse(oldItem.getDescription()));
        oldItem.setAvailable(updatedItem.getAvailable());

        return ItemMapper.modelToDto(itemStorage.update(oldItem, ownerId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Optional<Item> foundItem = itemStorage.findItemById(itemId);
        if (foundItem.isPresent()) {
            return ItemMapper.modelToDto(foundItem.get());
        } else {
            throw new NotFoundException(String.format(ITEM_NOT_FOUND_MSG));
        }
    }

    @Override
    public void deleteItem(int itemId) {
        itemStorage.deleteItem(itemId);
    }

    @Override
    public Collection<ItemDto> findAll(int ownerId) {
        return itemStorage.findAll(ownerId).stream()
                .map(ItemMapper::modelToDto)
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemStorage.searchItems(text);
    }
}
