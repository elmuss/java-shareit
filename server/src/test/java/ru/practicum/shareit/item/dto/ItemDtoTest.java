package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDtoTest {
    private final JacksonTester<ItemDto> json;

    @Test
    void testSerializeItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(null, null, null, null, null,
                null, null, null, null);
        itemDto.setId(4);
        itemDto.setName("name");
        itemDto.setDescription("description");
        itemDto.setLastBooking(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(10, 20, 10)));

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.description")
                .hasJsonPath("$.available")
                .hasJsonPath("$.ownerId")
                .hasJsonPath("$.requestId")
                .hasJsonPath("$.lastBooking")
                .hasJsonPath("$.nextBooking")
                .hasJsonPath("$.comments");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(itemDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(itemDto.getName()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo("description"));
        assertThat(result).extractingJsonPathStringValue("$.lastBooking")
                .satisfies(lastBooking -> assertThat(lastBooking).isEqualTo("2020-04-20T10:20:10"));
    }

}