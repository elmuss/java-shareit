package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class NewBookingDto {
    private Integer id;
    @NonNull
    private LocalDateTime start;
    @NonNull
    @Future
    private LocalDateTime end;
    private Integer itemId;
}
