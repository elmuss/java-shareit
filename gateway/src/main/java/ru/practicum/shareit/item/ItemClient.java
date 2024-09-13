package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.NewItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Map;


@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(int ownerId, NewItemDto newItem) {
        return post("", ownerId, newItem);
    }

    public ResponseEntity<Object> update(int id, UpdatedItemDto updatedItem, int ownerId) {
        return patch("/" + id, ownerId, updatedItem);
    }

    public ResponseEntity<Object> getItemById(int id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> findAll(int ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> deleteItemById(int id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> searchItems(String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", parameters);
    }

    public ResponseEntity<Object> createComment(int itemId, Comment newComment, int ownerId) {
        return post("/" + itemId + "/comment", ownerId, newComment);
    }
}
