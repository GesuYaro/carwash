package shagiev.carwash.model.user;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import shagiev.carwash.model.carbox.CarBox;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "operator_info")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OperatorInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "op_info_generator")
    @SequenceGenerator(name="op_info_generator", sequenceName = "op_info_seq", allocationSize=1)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private AppUser user;

    @OneToOne
    @JoinColumn(name = "carbox_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private CarBox carBox;

    @Column(name = "min_sale")
    private double minSale;

    @Column(name = "max_sale")
    private double maxSale;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OperatorInfo that = (OperatorInfo) o;
        return user != null && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
