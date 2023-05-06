package com.example.course.enities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "receptions")
//Приём
public class Reception {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String complaints;  //жалобы
    private String diagnostic;  //диагноз
    private String treatment;   //лечение

    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "registration_id")
    private Registration registration;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "reception_procedure",
            joinColumns = @JoinColumn(name = "reception_id"),
            inverseJoinColumns = @JoinColumn(name = "procedure_id"))
    private List<Procedure> procedure;

}
