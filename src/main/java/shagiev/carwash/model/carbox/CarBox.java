package shagiev.carwash.model.carbox;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "carbox")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CarBox {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carbox_generator")
    @SequenceGenerator(name="carbox_generator", sequenceName = "carbox_seq", allocationSize=1)
    private long id;

    @Column(name = "opening_time")
    private Time openingTime;

    @Column(name = "closing_time")
    private Time closingTime;

    @Column(name = "time_coefficient")
    private double timeCoefficient;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CarBox carBox = (CarBox) o;
        return Objects.equals(id, carBox.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
