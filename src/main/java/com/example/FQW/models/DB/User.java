package com.example.FQW.models.DB;

import com.example.FQW.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
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


}
