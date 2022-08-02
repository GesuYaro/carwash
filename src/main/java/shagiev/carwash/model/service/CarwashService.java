package shagiev.carwash.model.service;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Duration;
import java.util.Objects;

@Entity
@Table(name = "carwash_service")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class CarwashService {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_generator")
    @SequenceGenerator(name="service_generator", sequenceName = "service_seq", allocationSize=1)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private Duration duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CarwashService that = (CarwashService) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
