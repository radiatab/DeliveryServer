package fr.TabetSaidi.tp1Backend.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="delivery")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String addressFrom;
    @Column(columnDefinition = "TEXT")
    private String addressTo;
    private LocalDate deliveryDate;

    @Column(nullable = false)
    private double packageWeight;


    @ManyToOne
    @JoinColumn(name = "delivery_person_id")
    private DeliveryPerson deliveryPerson;

    @ManyToOne
    @JoinColumn(name = "tour_id")
    private Tour tour;


}
