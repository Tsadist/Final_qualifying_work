package com.example.kyrsovay.domain;

import com.example.kyrsovay.domain.enums.EmployeeRole;
import com.example.kyrsovay.domain.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private EmployeeRole employeeRole;

    @OneToMany(mappedBy = "employee")
    private List<Schedule> schedule = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "employee")
    private Set<Order> orders;

}
