package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserDto;
import ru.practicum.shareit.user.dto.UpdatedUserDto;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody NewUserDto newUser) {
        return userClient.create(newUser);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable @Min(1) int id, @RequestBody UpdatedUserDto updatedUser) {
        return userClient.update(id, updatedUser);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserById(@PathVariable @Min(1) int id) {
        return userClient.getUserById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> deleteUser(@PathVariable @Min(1) int id) {
        return userClient.deleteUserById(id);
    }
}
