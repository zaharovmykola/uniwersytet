package com.zakharov.mykola.uniwersytet.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
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

    @Size(min=3)
    @NotBlank(message = "Imie is mandatory")
    @Column(name = "imie", nullable = false, length = 100)
    private String imie;

    @NotBlank(message = "Nazwisko is mandatory")
    @Column(name = "nazwisko", nullable = false, length = 100)
    private String nazwisko;

    @Min(19)
    @NotBlank(message = "Wiek is mandatory")
    @Column(name="wiek", nullable = false)
    private Integer wiek;

    @Pattern(regexp="^([a-zA-Z0-9\\-\\.\\_]+)'+'(\\@)([a-zA-Z0-9\\-\\.]+)'+'(\\.)([a-zA-Z]{2,4})$")
    @NotBlank(message = "Email is mandatory")
    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Kierunek is mandatory")
    @Column(name = "kierunek", nullable = false, length = 100)
    private String kierunek;

    @ManyToMany
    @JoinTable(
            name = "student_nauczyciele",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "nauczyciel_id"))
    private Set<Nauczyciel> setofNauczyciele = new HashSet<>(0);

}
