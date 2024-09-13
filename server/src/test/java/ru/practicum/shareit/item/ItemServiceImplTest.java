package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RequestRepository requestRepository;

    @InjectMocks
    ItemServiceImpl itemService;

    @Test
    void create_whenUserIsFound_thenSaveItem() {
        int userId = 0;
        User user = User.builder().build();
        user.setId(userId);
        NewItemDto itemToSave = NewItemDto.builder().id(0).name("name").description("description").build();
        Item item = ItemMapper.modelFromNewItemDto(itemToSave);
        item.setUser(user);
        ItemDto expectedItem = ItemMapper.modelToDto(item);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ItemDto actualItem = itemService.create(itemToSave, userId);

        assertEquals(expectedItem, actualItem);
    }

    @Test
    void create_whenUserIsNotFound_returnException() {
        int userId = 0;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.create(NewItemDto.builder().build(), userId));
    }

    @Test
    void getItemById_whenItemFound_returnItem() {
        int itemId = 0;
        Item item = Item.builder().build();
        item.setUser(User.builder().build());
        ItemDto expectedItem = ItemMapper.modelToDto(item);
        expectedItem.setComments(List.of());

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ItemDto actualItem = itemService.getItemById(itemId);

        assertEquals(expectedItem, actualItem);
    }

    @Test
    void getItemById_whenItemNotFound_returnException() {
        int itemId = 0;
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(itemId));
    }

    @Test
    void update_whenFoundItem_updateItem() {
    }

    @Test
    void update_whenNotFoundItem_returnException() {
        int itemId = 0;
        int ownerId = 0;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.update(itemId, UpdatedItemDto.builder().build(), ownerId));
    }

    @Test
    void findAll() {
        int ownerId = 0;
        Collection<ItemBookingDto> expectedList = List.of();

        when(itemRepository.findByUser_Id(ownerId)).thenReturn(List.of());

        Collection<ItemBookingDto> actualList = itemService.findAll(ownerId);

        assertEquals(expectedList, actualList);
    }

    @Test
    void createComment() {
    }
}