package com.lhyla.interactivewebapp.data.entity;

import lombok.*;

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
    @Setter(AccessLevel.NONE)
    private Long id;

    private Date measurementDate;
    private BigDecimal value;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EngineeringUnit engineeringUnit;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Quality quality = Quality.BAD;

    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private Date dbCreationDate;

    @Column(updatable = false)
    @Setter(AccessLevel.NONE)
    private Date dbModifyDate;

    public enum EngineeringUnit {
        BARREL
    }

    public enum Quality {
        GOOD,
        BAD
    }
}