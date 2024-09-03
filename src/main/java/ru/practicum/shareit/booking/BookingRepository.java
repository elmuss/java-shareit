package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByUserId(int bookerId);

    @Query(value = "select b from Booking b where upper(b.status) like (upper(?1))" +
            "and b.user = ?2")
    List<Booking> findWaitingOrRejected(String text, Integer userId);

    List<Booking> findByEndBefore(LocalDateTime givenTime);

    List<Booking> findByStartAfter(LocalDateTime givenTime);

    List<Booking> findByStartBeforeAndEndAfter(LocalDateTime givenTimeStart, LocalDateTime givenTimeEnd);

    Optional<Booking> findByItemIdAndUserIdAndEndBefore(Integer itemId, Integer userId, LocalDateTime now);

    Booking findByItemIdAndStartBefore(Integer itemId, LocalDateTime now);

    Booking findByItemIdAndStartAfter(Integer itemId, LocalDateTime now);
}
