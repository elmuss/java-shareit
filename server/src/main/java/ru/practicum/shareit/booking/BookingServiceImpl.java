package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
    @Transactional
    public BookingDto create(NewBookingDto newBooking, Integer userId) {
        Booking booking = BookingMapper.modelFromNewBookingDto(newBooking);

        if (booking.getStart().equals(booking.getEnd())) {
            throw new ValidationException(WRONG_BOOKING_TIME_MSG);
        }

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND_MSG));

        booking.setUser(booker);
        Item itemForBooking = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND_MSG));


        if (!itemForBooking.getAvailable()) {
            throw new ValidationException(ITEM_IS_UNAVAILABLE_MSG);
        }

        booking.setItem(itemForBooking);
        booking.setStatus(Status.WAITING);
        return BookingMapper.modelToDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto update(int id, int userId, Boolean approved) {
        Booking bookingToUpdate = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND_MSG));

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(USER_NOT_FOUND_MSG));

        if (bookingToUpdate.getItem().getUser().getId() != userId) {
            throw new ValidationException(APPROVE_BY_WRONG_USER_MSG);
        }

        if (approved) {
            bookingToUpdate.setStatus(Status.APPROVED);
        } else {
            bookingToUpdate.setStatus(Status.REJECTED);
        }
        return BookingMapper.modelToDto(bookingRepository.save(bookingToUpdate));
    }

    @Override
    public BookingDto getBooking(int id, int userId) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(BOOKING_NOT_FOUND_MSG));

        if (booking.getUser().getId() == userId
                || booking.getItem().getUser().getId() == userId) {
            return BookingMapper.modelToDto(booking);
        } else {
            throw new ValidationException(WRONG_USER_MSG);
        }
    }

    @Override
    public List<BookingDto> getUsersBookingWithState(String state, int userId) {
        State enumState = stateConvert(state);
        List<BookingDto> bookingList = bookingRepository.findAllByUserId(userId).stream()
                .map(BookingMapper::modelToDto)
                .toList();

        bookingList = switch (enumState) {
            case State.CURRENT -> bookingRepository
                    .findByStartBeforeAndEndAfter(LocalDateTime.now(), LocalDateTime.now())
                    .stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case State.PAST -> bookingRepository.findByEndBefore(LocalDateTime.now()).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case State.FUTURE -> bookingRepository.findByStartAfter(LocalDateTime.now()).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            case State.WAITING, State.REJECTED -> bookingRepository.findWaitingOrRejected(state, userId).stream()
                    .map(BookingMapper::modelToDto)
                    .toList();
            default -> bookingList;
        };
        return bookingList;
    }

    public State stateConvert(String state) {
        return switch (state) {
            case "CURRENT" -> State.CURRENT;
            case "PAST" -> State.PAST;
            case "FUTURE" -> State.FUTURE;
            case "WAITING" -> State.WAITING;
            case "REJECTED" -> State.REJECTED;
            default -> State.ALL;
        };
    }

    @Override
    public List<BookingDto> getBookingWithStateByOwner(int id, State state) {
        return Collections.emptyList();
    }

}
