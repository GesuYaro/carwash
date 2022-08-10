package shagiev.carwash.model.entry;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import shagiev.carwash.model.carbox.CarBox;
import shagiev.carwash.model.service.CarwashService;
import shagiev.carwash.model.user.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "entry")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entry_generator")
    @SequenceGenerator(name="entry_generator", sequenceName = "entry_seq", allocationSize=1)
    private long id;

    @ManyToOne
    @JoinColumn(name = "carbox_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private CarBox carBox;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private CarwashService carwashService;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EntryStatus status;

    @Column(name = "price")
    private long price;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Fetch(FetchMode.JOIN)
    private AppUser user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Entry entry = (Entry) o;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
