package com.example.FQW.models.DB;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cleaner_application")
public class CleanersApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public enum Status {
        CREATED, OK, REJECTED
    }

    private Long orderId;
    private Long cleanerId;
    private String message;
    private Status status;
}
