package ru.practicum.shareit.request.dto;

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
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void testSerializeCommentDto() throws Exception {
        ItemRequestDto itemRequestDto = new ItemRequestDto(null, null, null,
                null, null);
        itemRequestDto.setId(3);
        itemRequestDto.setDescription("description");
        itemRequestDto.setRequesterId(8);
        itemRequestDto.setCreated(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(10, 20, 10)));

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requesterId")
                .hasJsonPath("$.created")
                .hasJsonPath("$.items");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(itemRequestDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .satisfies(description -> assertThat(description).isEqualTo(itemRequestDto.getDescription()));
        assertThat(result).extractingJsonPathNumberValue("$.requesterId")
                .satisfies(requesterId -> assertThat(requesterId.intValue()).isEqualTo(itemRequestDto.getRequesterId()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isEqualTo(itemRequestDto.getCreated().toString()));
    }

}