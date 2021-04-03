package com.zakharov.mykola.uniwersytet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="students")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imie", nullable = false, length = 100)
    private String imie;

    @Column(name = "nazwisko", nullable = false, length = 100)
    private String nazwisko;

    @Column(name="wiek")
    private Integer wiek;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "kierunek", nullable = false, length = 100)
    private String kierunek;

    @ManyToMany
    @JoinTable(
            name = "student_nauczyciele",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "nauczyciel_id"))
    Set<Nauczyciel> setofNauczyciele;

}
