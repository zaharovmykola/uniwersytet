package com.zakharov.mykola.uniwersytet.entity;

import javax.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.*;

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

    @NotBlank(message = "Przedmiot is mandatory")
    @Column(name = "przedmiot", nullable = false, length = 100)
    private String przedmiot;

    @ManyToMany(mappedBy = "setofNauczyciele", fetch = FetchType.LAZY)
    private Set<Student> setOfStudents = new HashSet<>(0);

}
