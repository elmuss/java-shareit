package ru.practicum.shareit.request;

import jakarta.validation.constraints.Min;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

public interface RequestService {
    ItemRequestDto create(ItemRequestDto newRequest, int requesterId);

    Collection<ItemRequestDto> findAllByUser(int requesterId);

    ItemRequestDto getRequestById(@Min(1) int id);

    Collection<ItemRequestDto> findAllByAnotherUsers(int requesterId);
}
