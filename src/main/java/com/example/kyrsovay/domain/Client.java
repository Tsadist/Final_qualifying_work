package com.example.kyrsovay.domain;

import com.example.kyrsovay.domain.enums.ClientRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fio;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private ClientRole clientRole;

    @OneToMany(mappedBy = "customer")
    private List<Order> orders = new ArrayList<>();

//    @OneToMany(mappedBy = "employee")
//    private Set<Order> orders;

}
