package com.example.kyrsach.enities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dentistry")
public class Dentistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


}
