package com.feelreal.api.model;

import com.feelreal.api.model.enumeration.WellnessCheckType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Table(name = "wellness_checks")
@NoArgsConstructor
public class WellnessCheck {
    @Transient
    private static final String VALUE_CONSTRAINT =
            "(type = 0 AND value BETWEEN 1 AND 10) OR (type = 1 AND value IN (11,12))";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type", nullable = false)
    private WellnessCheckType type;

    @Column(name = "value", nullable = false)
    @Check(constraints = VALUE_CONSTRAINT)
    private short value;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    public WellnessCheck(
            User user,
            WellnessCheckType type,
            short value,
            LocalDate date
    ) {
        this.user = user;
        this.type = type;
        this.value = value;
        this.date = date;
    }

}
