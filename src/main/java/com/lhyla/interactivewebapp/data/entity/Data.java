package com.lhyla.interactivewebapp.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@lombok.Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "data_generator")
    @SequenceGenerator(name = "data_generator", sequenceName = "data_seq")
    private Long id;
    private Date measurementDate;
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private EngineeringUnit engineeringUnit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quality quality = Quality.BAD;

    public enum EngineeringUnit {
        LITER,
        GALLON,
        BARREL
    }

    public enum Quality {
        GOOD,
        BAD
    }
}