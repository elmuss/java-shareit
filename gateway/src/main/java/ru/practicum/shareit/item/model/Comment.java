package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "text", nullable = false)
    private String text;
    @Column(name = "item_id")
    private Integer itemId;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;
    @Column(name = "creation_date")
    private LocalDateTime created;
}