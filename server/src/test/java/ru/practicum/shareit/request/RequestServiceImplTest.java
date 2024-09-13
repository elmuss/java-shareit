package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    RequestServiceImpl requestService;

    @Test
    void create_requesterFound_thenSaveRequest() {
        int requesterId = 0;
        ItemRequest request = ItemRequest.builder().build();
        request.setRequester(User.builder().build());
        ItemRequestDto expectedRequest = ItemRequestMapper.modelToDto(request);

        when(userRepository.findById(requesterId)).thenReturn(Optional.of(User.builder().id(requesterId).build()));

        ItemRequestDto actualRequest = requestService.create(expectedRequest, requesterId);

        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void create_requesterNotFound_throwException() {
        int requesterId = 0;

        when(userRepository.findById(requesterId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.create(ItemRequestDto.builder().build(), requesterId));
    }

    @Test
    void findAllByUser() {
        int userId = 0;
        Collection<ItemRequestDto> expectedList = List.of();

        when(requestRepository.findByRequesterId(userId)).thenReturn(List.of());

        Collection<ItemRequestDto> actualList = requestService.findAllByUser(userId);

        assertEquals(expectedList, actualList);
    }

    @Test
    void getRequestById_requestFound_thenReturnRequest() {
        int requestId = 0;
        ItemRequest request = ItemRequest.builder().build();
        request.setRequester(User.builder().build());
        ItemRequestDto expectedRequest = ItemRequestMapper.modelToDto(request);
        expectedRequest.setItems(List.of());

        when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));

        ItemRequestDto actualRequest = requestService.getRequestById(requestId);

        assertEquals(expectedRequest, actualRequest);
    }

    @Test
    void getRequestById_requestNotFound_throwException() {
        int requestId = 0;

        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequestById(requestId));
    }
}