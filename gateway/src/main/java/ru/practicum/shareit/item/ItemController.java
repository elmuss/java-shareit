package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") int ownerId,
                                         @RequestBody @Valid NewItemDto newItem) {
        log.info("Creating item {}, ownerId={}", newItem, ownerId);
        return itemClient.create(ownerId, newItem);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable @Min(1) int id,
                                         @RequestBody UpdatedItemDto updatedItem,
                                         @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemClient.update(id, updatedItem, ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@PathVariable @Min(1) int id) {
        return itemClient.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemClient.findAll(ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteItem(@PathVariable @Min(1) int id) {
        return itemClient.deleteItemById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItems(@RequestParam String text) {
        return itemClient.searchItems(text);
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@PathVariable @Min(1) int id,
                                         @RequestBody Comment newComment,
                                         @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemClient.createComment(id, newComment, ownerId);
    }
}
