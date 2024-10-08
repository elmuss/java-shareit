package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @RequestBody NewItemDto newItem, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.create(newItem, ownerId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto update(@PathVariable @Min(1) int id,
                          @RequestBody UpdatedItemDto updatedItem,
                          @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.update(id, updatedItem, ownerId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable @Min(1) int id) {
        return itemService.getItemById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<ItemBookingDto> findAll(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.findAll(ownerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable @Min(1) int id) {
        itemService.deleteItem(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{id}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable @Min(1) int id,
                             @RequestBody Comment newComment,
                             @RequestHeader("X-Sharer-User-Id") int ownerId) {
        return itemService.createComment(id, newComment, ownerId);
    }
}
