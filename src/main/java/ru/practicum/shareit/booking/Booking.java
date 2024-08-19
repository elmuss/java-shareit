package ru.practicum.shareit.booking;

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
    int id;
    LocalDate start;
    LocalDate end;
    int itemId;
    int bookerId;
    String status;

}
