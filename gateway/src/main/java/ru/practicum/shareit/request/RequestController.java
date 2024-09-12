package ru.practicum.shareit.request;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody ItemRequestDto newRequest,
                                         @RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestClient.create(requesterId, newRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByUser(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestClient.findAllByUser(requesterId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") int requesterId,
                                                 @PathVariable @Min(1) int id) {
        return requestClient.getRequestById(id, requesterId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAllByAnotherUsers(@RequestHeader("X-Sharer-User-Id") int requesterId) {
        return requestClient.findAllByAnotherUsers(requesterId);
    }
}
