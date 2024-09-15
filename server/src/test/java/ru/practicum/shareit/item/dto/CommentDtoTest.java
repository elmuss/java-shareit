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
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void testSerializeCommentDto() throws Exception {
        CommentDto commentDto = new CommentDto(null, null, null, null);
        commentDto.setId(3);
        commentDto.setText("comment");
        commentDto.setAuthorName("author");
        commentDto.setCreated(LocalDateTime.of(LocalDate.of(2020, Month.APRIL, 20),
                LocalTime.of(10, 20, 10)));

        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.text")
                .hasJsonPath("$.authorName")
                .hasJsonPath("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(commentDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.text")
                .satisfies(text -> assertThat(text).isEqualTo(commentDto.getText()));
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .satisfies(authorName -> assertThat(authorName).isEqualTo("author"));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isEqualTo("2020-04-20T10:20:10"));
    }
}