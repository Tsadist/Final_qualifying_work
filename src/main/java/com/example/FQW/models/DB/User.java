package com.example.FQW.models.DB;

import com.example.FQW.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String phoneNumber;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String activationCode;

    @Column(columnDefinition = "boolean default false not null")
    private boolean active;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Order> order = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Schedule> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "createUser")
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Message> messages = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, phoneNumber, email);
    }
}
