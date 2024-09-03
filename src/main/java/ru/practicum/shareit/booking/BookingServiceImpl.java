package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private static final String ITEM_IS_UNAVAILABLE_MSG = "Вещь недоступна для бронирования";
    private static final String ITEM_NOT_FOUND_MSG = "Вещь не найдена";
    private static final String BOOKING_NOT_FOUND_MSG = "Бронирование не найдено";
    private static final String USER_NOT_FOUND_MSG = "Пользователь не найден";
    private static final String WRONG_BOOKING_TIME_MSG = "Некорректное время бронирования";
    private static final String WRONG_USER_MSG = "Просомтр доступен только владельцу бронирования или владельцу вещи";
    private static final String APPROVE_BY_WRONG_USER_MSG = "Подтверждение бронирования доступно только владельцу вещи";

    @Override
    public BookingDto create(NewBookingDto newBooking, Integer userId) {
        Booking booking = BookingMapper.modelFromNewBookingDto(newBooking);

        if (booking.getStart().equals(booking.getEnd())) {
            throw new ValidationException(WRONG_BOOKING_TIME_MSG);
        }

        Optional<User> booker = userRepository.findById(userId);

        if (booker.isEmpty()) {
            throw new NotFoundException(USER_NOT_FOUND_MSG);
        }

        booking.setUser(booker.get());
        Optional<Item> itemForBooking = itemRepository.findById(newBooking.getItemId());


        if (itemForBooking.isEmpty()) {
            throw new NotFoundException(ITEM_NOT_FOUND_MSG);
        } else if (!itemForBooking.get().getAvailable()) {
            throw new ValidationException(ITEM_IS_UNAVAILABLE_MSG);
        }

        booking.setItem(itemForBooking.get());
        booking.setStatus(statusConvert(Status.WAITING));
        return BookingMapper.modelToDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto update(int id, int userId, Boolean approved) {
        Optional<Booking> bookingToUpdate = bookingRepository.findById(id);

        if (bookingToUpdate.isEmpty()) {
            throw new NotFoundException(BOOKING_NOT_FOUND_MSG);
        }

        Optional<User> booker = userRepository.findById(userId);

        if (booker.isEmpty()) {
            throw new ValidationException(USER_NOT_FOUND_MSG);
        }

        if (bookingToUpdate.get().getItem().getUser().getId() != userId) {
            throw new ValidationException(APPROVE_BY_WRONG_USER_MSG);
        }

        if (approved) {
            bookingToUpdate.get().setStatus(statusConvert(Status.APPROVED));
        } else {
            bookingToUpdate.get().setStatus(statusConvert(Status.REJECTED));
        }
        return BookingMapper.modelToDto(bookingRepository.save(bookingToUpdate.get()));
    }

    @Override
    public BookingDto getBooking(int id, int userId) {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            throw new NotFoundException(BOOKING_NOT_FOUND_MSG);
        }

        if (booking.get().getUser().getId() == userId
                || booking.get().getItem().getUser().getId() == userId) {
            return BookingMapper.modelToDto(booking.get());
        } else {
            throw new ValidationException(WRONG_USER_MSG);
        }
    }

    @Override
    public List<BookingDto> getUsersBookingWithState(String state, int userId) {
        List<BookingDto> bookingList = bookingRepository.findAllByUserId(userId).stream()
                .map(BookingMapper::modelToDto)
                .toList();
        LocalDateTime givenTime = LocalDateTime.now();
        LocalDateTime givenTimeEnd = LocalDateTime.now();

        bookingList = switch (state) {
            case "CURRENT" -> bookingRepository.findByStartBeforeAndEndAfter(givenTime, givenTimeEnd).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case "PAST" -> bookingRepository.findByEndBefore(givenTime).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case "FUTURE" -> bookingRepository.findByStartAfter(givenTime).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case "WAITING", "REJECTED" -> bookingRepository.findWaitingOrRejected(state, userId).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            default -> bookingList;
        };
        return bookingList;
    }

    @Override
    public List<BookingDto> getBookingWithStateByOwner(int id, String state) {
        return null;
    }

    public String statusConvert(Status status) {
        return switch (status) {
            case WAITING -> "WAITING";
            case APPROVED -> "APPROVED";
            case REJECTED -> "REJECTED";
            case CANCELED -> "CANCELED";
        };
    }
}
