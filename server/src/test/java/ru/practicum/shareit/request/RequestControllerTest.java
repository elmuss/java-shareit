package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(RequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestControllerTest {
    @MockBean
    private RequestService requestService;

    private final ApplicationContext applicationContext;
    private final ObjectMapper mapper;
    private final MockMvc mockMvc;

    @Test
    void create() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1).description("description").requesterId(1).build();

        when(requestService.create(itemRequestDto, 1)).thenReturn(itemRequestDto);

        mockMvc
                .perform(
                        post("/requests")
                                .content(mapper.writeValueAsString(itemRequestDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .header("X-Sharer-User-Id", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Integer.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.requesterId", is(itemRequestDto.getRequesterId()), Integer.class));

        verify(requestService, times(1)).create(itemRequestDto, 1);
    }

    @Test
    void findAllByUser() throws Exception {
        int requesterId = 0;
        when(requestService.findAllByUser(requesterId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requesterId))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(requestService, times(1)).findAllByUser(requesterId);
    }

    @Test
    void getRequestById() throws Exception {
        int requesterId = 0;
        int requestId = 1;
        ItemRequestDto request = ItemRequestDto.builder().id(requestId).build();

        when(requestService.getRequestById(requestId)).thenReturn(request);

        mockMvc.perform(get("/requests/{id}", requestId)
                        .header("X-Sharer-User-Id", requesterId))
                .andExpect(status().isOk());

        verify(requestService, times(1)).getRequestById(requestId);
    }
}