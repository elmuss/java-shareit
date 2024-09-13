package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@Valid @RequestBody NewBookingDto newBooking,
                             @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.create(newBooking, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto update(@PathVariable @Min(1) int id,
                             @RequestParam Boolean approved,
                             @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.update(id, userId, approved);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBooking(@PathVariable @Min(1) int id,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getBooking(id, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingWithState(@RequestParam(defaultValue = "ALL") String state,
                                                @RequestHeader("X-Sharer-User-Id") int userId) {
        return bookingService.getUsersBookingWithState(state, userId);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getBookingWithStateByOwner(@PathVariable @Min(1) int id,
                                                       @RequestParam(defaultValue = "ALL") State state) {
        return bookingService.getBookingWithStateByOwner(id, state);
    }
}
