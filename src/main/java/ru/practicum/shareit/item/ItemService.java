package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto create(NewItemDto newItem, int ownerId);

    ItemDto update(int itemId, UpdatedItemDto updatedItem, int ownerId);

    ItemDto getItemById(int itemId);

    void deleteItem(int itemId);

    Collection<ItemBookingDto> findAll(int ownerId);

    List<ItemDto> searchItems(String text);

    CommentDto createComment(int id, Comment newComment, int ownerId);
}
