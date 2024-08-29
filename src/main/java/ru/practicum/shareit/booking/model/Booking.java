package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Integer id;
    private LocalDate start;
    private LocalDate end;
    private Integer itemId;
    private Integer bookerId;
    private String status;

}
