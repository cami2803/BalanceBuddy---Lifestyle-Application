package com.example.balancebuddy.entities;

import com.example.balancebuddy.enums.Periodicity;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goals")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goalID")
    private int goalID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private MyUser user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periodicity")
    private Periodicity periodicity;

    @NotNull
    @Column(name = "target")
    private String target; // Target values are stored as a semicolon-separated string

    @NotNull
    @Column(name = "habits")
    private String habits; // Habits are stored as a semicolon-separated string

    @NotNull
    @Column(name = "progress")
    private String progress; // User's progress is stored as a semicolon-separated string
}
