package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(name = "creation_date")
    private LocalDateTime created;
}
