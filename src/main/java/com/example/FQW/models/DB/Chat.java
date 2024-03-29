package com.example.FQW.models.DB;

import com.example.FQW.models.enums.ChatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ChatStatus status;

    private String topic;
    private LocalDateTime createTime;
    private LocalDateTime lastModifiedTime;

    @ManyToOne
    @JoinColumn(name = "create_user_id")
    private User createUser;

    @OneToMany(mappedBy = "chat")
    private List<Message> messageList = new ArrayList<>();
}
