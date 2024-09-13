package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testSerializeBookingDto() throws Exception {
        BookingDto bookingDto = new BookingDto(null, null, null, null, null, null);
        Item newItem = new Item();
        User newUser = new User();

        bookingDto.setId(1);
        bookingDto.setStart(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(10, 20, 10)));
        bookingDto.setEnd(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(10, 20, 10)));
        bookingDto.setItem(newItem);
        bookingDto.setBooker(newUser);
        bookingDto.setStatus(Status.WAITING);

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.item")
                .hasJsonPath("$.booker")
                .hasJsonPath("$.status");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(bookingDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(start -> assertThat(start).isEqualTo(bookingDto.getStart().toString()));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(end -> assertThat(end).isEqualTo(bookingDto.getEnd().toString()));
        assertThat(result).extractingJsonPathStringValue("$.status")
                .satisfies(status -> assertThat(status).isEqualTo("WAITING"));
    }
}