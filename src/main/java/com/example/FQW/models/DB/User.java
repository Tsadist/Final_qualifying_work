package com.example.FQW.models.DB;

import com.example.FQW.models.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "usr")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @OneToOne(fetch = FetchType.LAZY)
//    private Client client;

    private String phoneNumber;
    private String password;
    private String email;
    private String name;
    private String surname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Order> order = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Schedule> schedule = new ArrayList<>();


}
