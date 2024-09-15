package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {BookingServiceImpl.class})
class BookingServiceImplTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    ApplicationContext applicationContext;
    @MockBean
    BookingRepository bookingRepository;
    @MockBean
    ItemRepository itemRepository;
    @MockBean
    UserRepository userRepository;

    private NewBookingDto newBookingDto;
    private BookingDto bookingDto;
    private Booking booking;

    @BeforeEach
    void setUp() {
        bookingRepository = mock(BookingRepository.class);
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        bookingService = new BookingServiceImpl(bookingRepository, itemRepository, userRepository);
        newBookingDto = NewBookingDto.builder().id(1).start(LocalDateTime.of(
                LocalDate.of(2025, Month.APRIL, 20),
                        LocalTime.of(10, 20, 10))).end(LocalDateTime.of(
                                LocalDate.of(2028, Month.APRIL, 20),
                        LocalTime.of(10, 20, 10))).itemId(1).build();

        booking = BookingMapper.modelFromNewBookingDto(newBookingDto);
        booking.setUser(User.builder().id(1).build());
        booking.setItem(Item.builder().id(1).build());
        bookingDto = BookingMapper.modelToDto(booking);
    }

    @Test
    void create() {
        when(userRepository.findById(1)).thenReturn(Optional.of(User.builder().id(1)
                .name("name").email("email@email").build()));
        when(itemRepository.findById(1)).thenReturn(Optional.of(Item.builder().id(1).name("name")
                .description("description").available(true).build()));

        BookingDto result = bookingService.create(newBookingDto, 1);
        Assertions.assertNotNull(result);
        verify(bookingRepository, times(1)).save(any());
    }

    @Test
    void getBooking() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(User.builder().id(1)
                .name("name").email("email@email").build()));

        BookingDto result = bookingService.getBooking(booking.getId(), 1);
        Assertions.assertNotNull(result);
        verify(bookingRepository, times(1)).findById(booking.getId());
    }

    @Test
    void getUsersBookingWithState() {
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(userRepository.findById(1)).thenReturn(Optional.of(User.builder().id(1)
                .name("name").email("email@email").build()));

        List<BookingDto> result = bookingService.getUsersBookingWithState("ALL", 1);
        Assertions.assertNotNull(result);
        verify(bookingRepository, times(1)).findAllByUserId(booking.getId());
    }
}