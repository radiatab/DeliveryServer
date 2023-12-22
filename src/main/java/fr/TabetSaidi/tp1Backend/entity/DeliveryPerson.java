package fr.TabetSaidi.tp1Backend.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "deliveryperson")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPerson implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = false)
    private boolean available;

    @Column(name = "creationDate")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "deliveryPerson")
    private List<Delivery> deliveries;

    @ManyToMany
    @JoinTable(
            name = "deliveryperson_tour",
            joinColumns = @JoinColumn(name = "deliveryperson_id"),
            inverseJoinColumns = @JoinColumn(name = "tour_id")
    )
    private List<Tour> tours;

    public void addTour(Tour tour) {
        if (this.tours == null) {
            this.tours = new ArrayList<>(); // Initialisation de la liste si elle est null
        }

        if (!this.tours.isEmpty()) {
            throw new IllegalStateException("Ce livreur est déjà associé à une tournée en cours.");
        }

        this.tours.add(tour);
        tour.setDeliveryPerson(this);
    }
}