package com.zakharov.mykola.uniwersytet.entity;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="nauczyciele")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Nauczyciel {

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

    @Column(name = "przedmiot", nullable = false, length = 100)
    private String przedmiot;

    @ManyToMany(mappedBy = "student_nauczyciele")
    private Set<Student> setOfStudents = new HashSet<>(0);

}
