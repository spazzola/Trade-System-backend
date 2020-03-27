package com.tradesystem.cost;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "costs")
public class Cost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long costId;

    private String name;

    private BigDecimal value;

    private LocalDate date;


    public Cost(String name, BigDecimal value, LocalDate date) {
        this.name = name;
        this.value = value;
        this.date = date;
    }
}
