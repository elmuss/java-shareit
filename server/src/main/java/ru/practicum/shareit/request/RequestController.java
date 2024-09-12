package ru.practicum.shareit.request;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto create(@RequestBody ItemRequestDto newRequest,
                                 @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestService.create(newRequest, requesterId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findAllByUser(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestService.findAllByUser(requesterId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                         @PathVariable @Min(1) int id) {
        return requestService.getRequestById(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemRequestDto> findAllByAnotherUsers(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestService.findAllByAnotherUsers(requesterId);
    }
}
