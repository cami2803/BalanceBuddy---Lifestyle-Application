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
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "habitid")
    @Getter
    @Setter
    private int habitID;

    @NotNull
    @Column(name = "name")
    @Getter
    @Setter
    private String name;

    @NotNull
    @Column(name = "unit")
    @Getter
    @Setter
    private String unit;
}
