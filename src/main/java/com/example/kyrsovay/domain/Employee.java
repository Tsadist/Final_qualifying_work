package com.example.kyrsovay.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Client client;

    private String phoneNumber;

//    private String username;
//    private String password;
//    private String email;

//    @Enumerated(EnumType.STRING)
//    private UserRole userRole;

    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private List<Order> order;


}
