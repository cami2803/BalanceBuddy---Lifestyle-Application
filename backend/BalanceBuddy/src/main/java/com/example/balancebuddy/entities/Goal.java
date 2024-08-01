package com.example.balancebuddy.entities;

import com.example.balancebuddy.enums.Periodicity;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goalID")
    @Getter
    @Setter
    private int goalID;

    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    @Getter
    @Setter
    private MyUser user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "periodicity")
    @Getter
    @Setter
    private Periodicity periodicity;

    @NotNull
    @Column(name = "target")
    @Getter
    @Setter
    private String target; // Target values are stored as a semicolon-separated string

    @NotNull
    @Column(name = "habits")
    @Getter
    @Setter
    private String habits; // Habits are stored as a semicolon-separated string
}
