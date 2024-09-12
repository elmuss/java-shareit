package ru.practicum.shareit.request.mapper;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto modelToDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requesterId(request.getRequester().getId())
                .created(request.getCreated())
                .build();
    }

    public static ItemRequest modelFromDto(ItemRequestDto requestDto) {
        return ItemRequest.builder()
                .id(requestDto.getId())
                .description(requestDto.getDescription())
                .created(requestDto.getCreated())
                .build();
    }
}
