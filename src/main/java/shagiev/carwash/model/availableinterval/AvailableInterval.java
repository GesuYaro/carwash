package shagiev.carwash.model.availableinterval;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import shagiev.carwash.model.carbox.CarBox;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "available_interval")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AvailableInterval {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "interval_generator")
    @SequenceGenerator(name="interval_generator", sequenceName = "interval_seq", allocationSize=1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "carbox_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private CarBox carBox;

    @Column(name = "from_time")
    private long from; // unix time

    @Column(name = "until_time")
    private long until; // unix time

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AvailableInterval that = (AvailableInterval) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
