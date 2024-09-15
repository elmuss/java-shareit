package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    @MockBean
    private BookingService bookingService;

    private final ApplicationContext applicationContext;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    NewBookingDto newBookingDto = NewBookingDto.builder().id(1).start(LocalDateTime.of(
            LocalDate.of(2025, Month.APRIL, 20),
                    LocalTime.of(10, 20, 10))).end(LocalDateTime.of(
                            LocalDate.of(2028, Month.APRIL, 20),
                    LocalTime.of(10, 20, 10))).itemId(1).build();
    BookingDto bookingDto = BookingDto.builder().id(1).start(LocalDateTime.of(
            LocalDate.of(2025, Month.APRIL, 20),
                    LocalTime.of(10, 20, 10))).end(LocalDateTime.of(
                            LocalDate.of(2028, Month.APRIL, 20),
                    LocalTime.of(10, 20, 10))).item(null).booker(null).status(null).build();

    @Test
    void create() throws Exception {
        when(bookingService.create(newBookingDto, 1)).thenReturn(bookingDto);

        mockMvc
                .perform(
                        post("/bookings")
                                .content(mapper.writeValueAsString(newBookingDto))
                                .header("X-Sharer-User-Id", 1)
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newBookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is("2025-04-20T10:20:10")))
                .andExpect(jsonPath("$.end", is("2028-04-20T10:20:10")));
        verify(bookingService, times(1)).create(newBookingDto, 1);
    }

    @Test
    void update() throws Exception {
        when(bookingService.update(1, 1, true)).thenReturn(bookingDto);

        mockMvc
                .perform(
                        patch("/bookings/{id}", 1)
                                .header("X-Sharer-User-Id", 1)
                                .param("approved", "true")
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newBookingDto.getId()), Integer.class))
                .andExpect(jsonPath("$.start", is("2025-04-20T10:20:10")))
                .andExpect(jsonPath("$.end", is("2028-04-20T10:20:10")));
        verify(bookingService, times(1)).update(1, 1, true);
    }

    @Test
    void getBooking() throws Exception {
        when(bookingService.getBooking(1, 1)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{id}", 1)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newBookingDto.getId()), Integer.class));
        verify(bookingService, times(1)).getBooking(1, 1);
    }

    @Test
    void getBookingWithState() throws Exception {
        when(bookingService.getUsersBookingWithState("ALL", 1)).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", ""))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"start\":\"2025-04-20T10:20:10\"," +
                        "\"end\":\"2028-04-20T10:20:10\",\"item\":null,\"booker\":null,\"status\":null}]"));

        verify(bookingService, times(1)).getUsersBookingWithState("ALL", 1);
    }
}