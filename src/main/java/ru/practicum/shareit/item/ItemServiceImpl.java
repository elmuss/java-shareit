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

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;

    private static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String WRONG_OWNER_ID_ERROR = "Обновление доступно только владельцу";

    @Override
    public ItemDto create(ItemDto newItemDto, int ownerId) {
        Item newItem = ItemMapper.modelFromDto(newItemDto);

        if (userService.getUserById(ownerId) == null) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        }

        itemStorage.create(newItem, ownerId);
        return ItemMapper.modelToDto(newItem);
    }

    @Override
    public ItemDto update(int itemId, ItemDto updatedItemDto, int ownerId) {
        if (itemStorage.findItemById(itemId).getOwnerId() != ownerId) {
            throw new NotFoundException(WRONG_OWNER_ID_ERROR);
        }
        return ItemMapper.modelToDto(itemStorage.update(itemId, updatedItemDto));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.modelToDto(itemStorage.findItemById(itemId));
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
    public List<ItemDto> searchItems(String text) {
        return itemStorage.searchItems(text).stream()
                .map(ItemMapper::modelToDto)
                .toList();
    }
}
