package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemControllerTest {
    @MockBean
    ItemService itemService;

    private final ApplicationContext applicationContext;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @Test
    void create() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder().id(1).name("name").description("description")
                .available(true).ownerId(1).build();
        ItemDto itemDto = ItemDto.builder().id(1).name("name").description("description")
                .available(true).ownerId(1).build();

        when(itemService.create(newItemDto, 1)).thenReturn(itemDto);

        mockMvc
                .perform(
                        post("/items")
                                .content(mapper.writeValueAsString(newItemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header("X-Sharer-User-Id", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newItemDto.getId()), Integer.class))
                .andExpect(jsonPath("$.name", is(newItemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(newItemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.ownerId", is(newItemDto.getOwnerId()), Integer.class))
                .andExpect(jsonPath("$.available").exists());

        verify(itemService, times(1)).create(newItemDto, 1);
    }

    @Test
    void getItemById() throws Exception {
        NewItemDto newItemDto = NewItemDto.builder().id(1).name("name").description("description")
                .available(true).ownerId(1).build();
        Item item = ItemMapper.modelFromNewItemDto(newItemDto);
        item.setUser(User.builder().build());
        ItemDto itemDto = ItemMapper.modelToDto(item);

        when(itemService.getItemById(1)).thenReturn(itemDto);

        mockMvc.perform(get("/items/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "    \"id\": 1,\n" +
                        "    \"name\": \"name\",\n" +
                        "    \"description\": \"description\",\n" +
                        "    \"available\": true,\n" +
                        "    \"ownerId\": null,\n" +
                        "    \"requestId\": null,\n" +
                        "    \"lastBooking\": null,\n" +
                        "    \"nextBooking\": null,\n" +
                        "    \"comments\": null\n" +
                        "}"));

        verify(itemService, times(1)).getItemById(1);
    }

    @Test
    void findAll() throws Exception {
        when(itemService.findAll(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(itemService, times(1)).findAll(1);
    }
}