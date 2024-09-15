package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.NewBookingDto;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(int userId, NewBookingDto newBooking) {
        return post("", userId, newBooking);
    }

    public ResponseEntity<Object> update(int id, Boolean approved, long userId) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );

        return patch("/" + id + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> getBooking(int id, int userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> getUsersBookingWithState(String state, long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("?state={state}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingWithStateByOwner(String state, long userId) {
        Map<String, Object> parameters = Map.of(
                "state", state
        );
        return get("/owner?state={state}", userId, parameters);
    }
}
