package com.example.balancebuddy.entities;


import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "history")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "historyID")
    private int historyID;

    @JoinColumn(name = "userID", nullable = false)
    private int userID;

    @NotNull
    @Column(name = "date")
    private LocalDate date;

    @NotNull
    @Column(name = "percentage")
    private double percentage;

    public History(int userID, LocalDate date, double percentage) {
        this.userID = userID;
        this.date = date;
        this.percentage = percentage;
    }
}
