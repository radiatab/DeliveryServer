package fr.TabetSaidi.tp1Backend.repository;

import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson,Long> {
    @Query("SELECT dp FROM DeliveryPerson dp LEFT JOIN dp.tours t GROUP BY dp.id ORDER BY COUNT(t) DESC")
    List<DeliveryPerson> trierLivreursParNombreTournées();
    Optional<DeliveryPerson> findById(Long id);

}

