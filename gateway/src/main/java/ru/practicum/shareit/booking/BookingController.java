package ru.practicum.shareit.booking;

import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.NewBookingDto;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@Valid @RequestBody NewBookingDto newBooking,
                                         @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingClient.create(userId, newBooking);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(@PathVariable @Min(1) int id,
                                         @RequestParam Boolean approved,
                                         @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.update(id, approved, userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBooking(@PathVariable @Min(1) int id,
                                             @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingClient.getBooking(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingWithState(@RequestParam(defaultValue = "ALL") String state,
                                                      @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingClient.getUsersBookingWithState(state, userId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingWithStateByOwner(@PathVariable @Min(1) long id,
                                                             @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getBookingWithStateByOwner(state, id);
    }
}
