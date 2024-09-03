package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByUser_Id(int userId);

    @Query(value = "select i from Item i where upper(i.name) like concat('%', upper(?1), '%')" +
            "or upper(i.description) like concat('%', upper(?1), '%')")
    List<Item> findByText(String text);
}
