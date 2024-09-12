package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findByRequesterId(int userId);

    List<ItemRequest> findByRequesterIdNot(int userId);
}
