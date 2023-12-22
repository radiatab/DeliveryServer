package fr.TabetSaidi.tp1Backend.service;
import fr.TabetSaidi.tp1Backend.dto.TourDto;
import fr.TabetSaidi.tp1Backend.entity.Delivery;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.entity.Tour;
import fr.TabetSaidi.tp1Backend.repository.DeliveryPersonRepository;
import fr.TabetSaidi.tp1Backend.repository.DeliveryRepository;
import fr.TabetSaidi.tp1Backend.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class TourService {

    private final TourRepository tourRepository;
    private final DeliveryPersonRepository deliveryPersonRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    public TourService(TourRepository tourRepository, DeliveryPersonRepository deliveryPersonRepository) {
        this.tourRepository = tourRepository;
        this.deliveryPersonRepository = deliveryPersonRepository;
    }
    public TourDto createTourForDeliveryPerson(TourDto tourDto, Long deliveryPersonId) {
        // Vérifier si le livreur existe
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(deliveryPersonId)
                .orElseThrow(() -> new IllegalArgumentException("Livreur non trouvé avec l'ID : " + deliveryPersonId));

        // Vérifier si le livreur a déjà une tournée
        if (deliveryPerson.getTours() != null && !deliveryPerson.getTours().isEmpty()) {
            throw new IllegalStateException("Ce livreur est déjà associé à une tournée en cours.");
        }

        // Convertir TourDto en entité Tour
        Tour tour = TourDto.toEntity(tourDto);
        tour.setDeliveryPerson(deliveryPerson); // Associer le livreur à la tournée

        // Enregistrer la tournée dans la base de données
        Tour savedTour = tourRepository.save(tour);

        return TourDto.fromEntity(savedTour);
    }

    public TourDto getTourById(Long id) {
        Optional<Tour> tourOptional = tourRepository.findById(id);
        return tourOptional.map(TourDto::fromEntity).orElse(null);
    }
    public TourDto updateTour(Long id, TourDto updatedTourDto) {
        Optional<Tour> existingTourOptional = tourRepository.findById(id);
        if (existingTourOptional.isPresent()) {
            Tour existingTour = existingTourOptional.get();

            // Mettre à jour les champs nécessaires
            existingTour.setTourName(updatedTourDto.getTourName());
            existingTour.setDescription(updatedTourDto.getDescription());
            existingTour.setStartDate(updatedTourDto.getStartDate());
            existingTour.setEndDate(updatedTourDto.getEndDate());
            // Mettre à jour d'autres champs si nécessaire

            Tour savedTour = tourRepository.save(existingTour);
            return TourDto.fromEntity(savedTour);
        } else {
            return null;
        }
    }
    public List<Tour> getAllTour() {
        return tourRepository.findAll();
    }
    //Delete
    public boolean deleteTourById(Long id) {
        try {
            tourRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // Gérer l'exception ou loguer le message d'erreur si la suppression échoue
            return false;
        }
    }
   //E_40
    public boolean addDeliveriesToTour(Long tourId, List<Long> deliveryIds) {
        Optional<Tour> tourOptional = tourRepository.findById(tourId);
        if (tourOptional.isPresent()) {
            Tour tour = tourOptional.get();

            // Récupérez les livraisons correspondant aux IDs fournis
            List<Delivery> deliveries = deliveryRepository.findAllById(deliveryIds);

            // Associez les livraisons à la tournée
            tour.getDeliveries().addAll(deliveries);

            // Sauvegardez la tournée mise à jour dans la base de données
            tourRepository.save(tour);
            return true;
        } else {
            return false;
        }
    }
    //E_50
    public Page<TourDto> getToursByDeliveryPersonId(Long deliveryPersonId, Pageable pageable) {
        return tourRepository.findByDeliveryPersonId(deliveryPersonId, pageable)
                .map(TourDto::fromEntity);
    }

    //E_60
    public Page<TourDto> getToursByDate(LocalDate date, Pageable pageable) {
        return tourRepository.findByStartDate(date, pageable)
                .map(TourDto::fromEntity);
    }

    //E_80
    public TourDto getTourDetailsById(Long tourId) {
        return tourRepository.findById(tourId)
                .map(TourDto::fromEntity)
                .orElse(null);
    }
}
