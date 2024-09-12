package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;

    private static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String REQUEST_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String WRONG_OWNER_ID_ERROR = "Обновление доступно только владельцу";
    private static final String WRONG_ITEM_FOR_COMMENT_ERROR = "Невозможно оставить комментарий," +
            "так как вы не бронировали данную вещь";

    @Override
    @Transactional
    public ItemDto create(NewItemDto newItemDto, int ownerId) {
        Item newItem = ItemMapper.modelFromNewItemDto(newItemDto);
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        if (newItemDto.getRequestId() != null) {
            ItemRequest request = requestRepository.findById(newItemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND_MSG));

            newItem.setRequest(request);
        }

        newItem.setUser(owner);
        itemRepository.save(newItem);

        return ItemMapper.modelToDto(newItem);
    }

    @Override
    @Transactional
    public ItemDto update(int itemId, UpdatedItemDto updatedItemDto, int ownerId) {
        Item oldItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_MSG));

        if (oldItem.getUser().getId() != ownerId) {
            throw new NotFoundException(WRONG_OWNER_ID_ERROR);
        }

        Item updatedItem = ItemMapper.updateItemFields(oldItem, updatedItemDto);

        return ItemMapper.modelToDto(itemRepository.save(updatedItem));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        Item foundItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_MSG));

        ItemDto getItem = ItemMapper.modelToDto(foundItem);

        getItem.setComments(commentRepository.findByItemId(itemId).stream()
                .map(CommentMapper::modelToDto)
                .toList());

        return getItem;
    }

    @Override
    @Transactional
    public void deleteItem(int itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_MSG));
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
    @Transactional
    public CommentDto createComment(int id, Comment newComment, int ownerId) {
        Booking bookingList = bookingRepository.findByItemIdAndUserIdAndEndBefore(id, ownerId, LocalDateTime.now())
                .orElseThrow(() -> new ValidationException(WRONG_ITEM_FOR_COMMENT_ERROR));

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        newComment.setAuthor(owner);
        newComment.setItemId(id);
        newComment.setCreated(LocalDateTime.now());

        return CommentMapper.modelToDto(commentRepository.save(newComment));
    }
}
