package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.ItemShort;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String REQUEST_NOT_FOUND_MSG = "Запрос не найден";

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto newRequest, int requesterId) {
        newRequest.setRequesterId(requesterId);
        newRequest.setCreated(LocalDateTime.now());

        ItemRequest request = ItemRequestMapper.modelFromDto(newRequest);
        User owner = userRepository.findById(requesterId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        request.setRequester(owner);
        requestRepository.save(request);

        return ItemRequestMapper.modelToDto(request);
    }

    @Override
    public Collection<ItemRequestDto> findAllByUser(int requesterId) {
        return requestRepository.findByRequesterId(requesterId).stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
    }

    @Override
    public ItemRequestDto getRequestById(int id) {
        ItemRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(REQUEST_NOT_FOUND_MSG));
        ItemRequestDto getRequest = ItemRequestMapper.modelToDto(request);
        List<ItemShort> responses = itemRepository.findByRequestId(id);
        getRequest.setItems(responses);

        return getRequest;
    }

    @Override
    public Collection<ItemRequestDto> findAllByAnotherUsers(int requesterId) {
        return requestRepository.findByRequesterIdNot(requesterId).stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
    }
}
