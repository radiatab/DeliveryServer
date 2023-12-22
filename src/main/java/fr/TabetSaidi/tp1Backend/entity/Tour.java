package fr.TabetSaidi.tp1Backend.entity;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tour")
public class Tour implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tourName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToMany(mappedBy = "tours")
    private List<DeliveryPerson> deliveryPersons;

  @ManyToMany
    @JoinTable(
            name = "tour_delivery",
            joinColumns = @JoinColumn(name = "tour_id"),
            inverseJoinColumns = @JoinColumn(name = "delivery_id")
    )
    private List<Delivery> deliveries;

    /*@OneToMany
    @JoinColumn(name = "tour_id")
    private List<Delivery> deliveries;*/


    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryPerson deliveryPerson;

    // Méthode pour associer un livreur à une tournée
    public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
        if (!deliveryPerson.getTours().contains(this)) {
            deliveryPerson.addTour(this);
        }
    }
    public List<DeliveryPerson> getDeliveryPersons() {
        if (deliveryPersons == null) {
            deliveryPersons = new ArrayList<>();
        }
        return deliveryPersons;
    }


}
