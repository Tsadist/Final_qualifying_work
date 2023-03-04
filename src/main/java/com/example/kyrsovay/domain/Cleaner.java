package com.example.kyrsovay.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(fetch = FetchType.LAZY)
    private Client client;

    private String name;
    private String surname;

    @OneToMany(mappedBy = "cleaner")
    private List<Schedule> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "cleaner")
    private List<Order> order = new ArrayList<>();

}
