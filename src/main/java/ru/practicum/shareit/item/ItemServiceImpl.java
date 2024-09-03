package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    private static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String WRONG_OWNER_ID_ERROR = "Обновление доступно только владельцу";
    private static final String WRONG_ITEM_FOR_COMMENT_ERROR = "Невозможно оставить комментарий," +
            "так как вы не бронировали данную вещь";

    @Override
    public ItemDto create(NewItemDto newItemDto, int ownerId) {
        Item newItem = ItemMapper.modelFromNewItemDto(newItemDto);
        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        } else {
            newItem.setUser(owner.get());
        }

        itemRepository.save(newItem);
        return ItemMapper.modelToDto(newItem);
    }

    @Override
    public ItemDto update(int itemId, UpdatedItemDto updatedItemDto, int ownerId) {
        Optional<Item> oldItem = itemRepository.findById(itemId);

        if (oldItem.isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        }
        if (oldItem.get().getUser().getId() != ownerId) {
            throw new NotFoundException(WRONG_OWNER_ID_ERROR);
        }

        Item updatedItem = ItemMapper.updateItemFields(oldItem.get(), updatedItemDto);

        return ItemMapper.modelToDto(itemRepository.save(updatedItem));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Optional<Item> foundItem = itemRepository.findById(itemId);

        if (foundItem.isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        }

        ItemDto getItem = ItemMapper.modelToDto(foundItem.get());

        getItem.setComments(commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::modelToDto)
                .toList());

        return getItem;
    }

    @Override
    public void deleteItem(int itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public Collection<ItemBookingDto> findAll(int ownerId) {
        return itemRepository.findByUser_Id(ownerId).stream()
                .map(ItemMapper::modelToBookingDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return List.of();
        }

        return itemRepository.findByText(text).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::modelToDto)
                .toList();
    }

    @Override
    public CommentDto createComment(int id, Comment newComment, int ownerId) {
        Optional<Booking> bookingList =
                bookingRepository.findByItemIdAndUserIdAndEndBefore(id, ownerId, LocalDateTime.now());

        if (bookingList.isEmpty()) {
            throw new ValidationException(WRONG_ITEM_FOR_COMMENT_ERROR);
        }

        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        } else {
            newComment.setAuthor(owner.get());
        }

        newComment.setItemId(id);
        newComment.setCreated(LocalDateTime.now());
        return CommentMapper.modelToDto(commentRepository.save(newComment));
    }
}
