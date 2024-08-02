package com.example.balancebuddy.entities;

import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "habits")
@Getter
@Setter
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habitid")
    private int habitID;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "unit")
    private String unit;
}
