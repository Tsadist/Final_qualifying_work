package com.example.kyrsovay.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "cleaner")
public class Cleaner {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Client client;

    private String name;
    private String surname;

    @OneToMany(mappedBy = "cleaner")
    private List<Schedule> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Order> order = new ArrayList<>();

}
