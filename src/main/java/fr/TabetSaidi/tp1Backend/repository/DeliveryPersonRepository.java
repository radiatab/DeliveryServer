package fr.TabetSaidi.tp1Backend.repository;

import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson,Long> {



}
