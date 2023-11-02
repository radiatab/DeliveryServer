package fr.TabetSaidi.tp1Backend.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="delivery")
public class DeliveryPerson implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean available;
    @Column(name = "creationDate")
    private LocalDate creationDate;

    public DeliveryPerson() {
    }

    public DeliveryPerson(Long id, String name, boolean available) {
        this.id = id;
        this.name = name;
        this.available = available;
    }

    public DeliveryPerson(String name, boolean available) {
        this.name = name;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

}
