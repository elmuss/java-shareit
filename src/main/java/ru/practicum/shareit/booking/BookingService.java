package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.util.List;

public interface BookingService {
    BookingDto create(NewBookingDto newBooking, Integer userId);

    BookingDto update(int id, int userId, Boolean approved);

    BookingDto getBooking(int id, int userId);

    List<BookingDto> getUsersBookingWithState(String state, int userId);

    List<BookingDto> getBookingWithStateByOwner(int id, State state);
}
