package fr.TabetSaidi.tp1Backend.repository;
import fr.TabetSaidi.tp1Backend.entity.Delivery;
import fr.TabetSaidi.tp1Backend.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    int countToursByDeliveryPersonsId(Long id);
    Page<Tour> findByDeliveryPersonId(Long deliveryPersonId, Pageable pageable);
    Page<Tour> findByStartDate(LocalDate startDate, Pageable pageable);


}
