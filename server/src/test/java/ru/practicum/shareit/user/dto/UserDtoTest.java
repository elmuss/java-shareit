package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoTest {
    private final JacksonTester<UserDto> json;

    @Test
    void testSerializeUserDto() throws Exception {
        UserDto userDto = new UserDto(null, null, null);
        userDto.setId(1);
        userDto.setName("name");
        userDto.setEmail("email@email");

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.name")
                .hasJsonPath("$.email");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.intValue()).isEqualTo(userDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
                .satisfies(name -> assertThat(name).isEqualTo(userDto.getName()));
        assertThat(result).extractingJsonPathStringValue("$.email")
                .satisfies(email -> assertThat(email).isEqualTo(userDto.getEmail()));
    }

}