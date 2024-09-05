package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import lombok.*;

import java.time.LocalDateTime;

@Data
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
