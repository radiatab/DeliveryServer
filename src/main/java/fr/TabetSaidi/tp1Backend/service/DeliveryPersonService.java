package fr.TabetSaidi.tp1Backend.service;

import fr.TabetSaidi.tp1Backend.dto.DeliveryPersonDetailsDTO;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.repository.DeliveryPersonRepository;
import fr.TabetSaidi.tp1Backend.repository.DeliveryRepository;
import fr.TabetSaidi.tp1Backend.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;

    @Autowired
    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository, DeliveryRepository deliveryRepository, TourRepository tourRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
    }

    public DeliveryPersonDetailsDTO createDeliveryPerson(DeliveryPerson deliveryPerson) {
        deliveryPerson.setCreationDate(LocalDate.now());
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

        int deliveryCount = deliveryRepository.countDeliveriesByDeliveryPersonId(savedDeliveryPerson.getId());
        int tourCount = tourRepository.countToursByDeliveryPersonsId(savedDeliveryPerson.getId());

        DeliveryPersonDetailsDTO deliveryPersonDetailsDTO = new DeliveryPersonDetailsDTO(savedDeliveryPerson, deliveryCount, 0);
        deliveryPersonDetailsDTO.setTourCount(tourCount);

        return deliveryPersonDetailsDTO;
    }



    public List<DeliveryPerson> getAllDeliveryPersons() {
        return deliveryPersonRepository.findAll();
    }

    public Page<DeliveryPerson> getAllDeliveryPersonsPage(Pageable pageable) {
        // Votre logique pour récupérer la liste paginée de livreurs
        return deliveryPersonRepository.findAll(pageable);
    }


    public void deleteDelivery(Long id) {
        deliveryPersonRepository.deleteById(id);

    }

    public DeliveryPerson getDeliveryPersonById(Long id) {
        return deliveryPersonRepository.findById(id).orElse(null);
    }

    public void deleteDeliveryPerson(Long id) {
        // Vérifiez d'abord si le livreur existe
        if (deliveryPersonRepository.existsById(id)) {
            // Supprimez le livreur s'il existe
            deliveryPersonRepository.deleteById(id);
        } else {
            throw new RuntimeException("Livreur non trouvé avec l'ID : " + id);
        }


    }

    public DeliveryPerson updateDeliveryPerson(Long id, DeliveryPerson updatedDeliveryPerson) {
        // Vérifiez d'abord si le livreur existe
        if (deliveryPersonRepository.existsById(id)) {
            // Obtenez le livreur existant
            DeliveryPerson existingPerson = deliveryPersonRepository.findById(id).orElse(null);

            // Mettez à jour les champs du livreur existant avec les données mises à jour
            if (existingPerson != null) {
                existingPerson.setName(updatedDeliveryPerson.getName());
                existingPerson.setAvailable(updatedDeliveryPerson.isAvailable());
                // Vous pouvez mettre à jour d'autres champs ici
            }

            // Enregistrez les modifications dans la base de données
            existingPerson = deliveryPersonRepository.save(existingPerson);

            return existingPerson;
        } else {
            throw new RuntimeException("Livreur non trouvé avec l'ID : " + id);
        }
    }

    public List<DeliveryPerson> searchDeliveryPersons(
            Boolean available,
            LocalDate creationDate,
            LocalDate creationDateAfter,
            LocalDate creationDateBefore,
            LocalDate creationDateBetweenStart,
            LocalDate creationDateBetweenEnd,
            String sortBy) {
        List<DeliveryPerson> allDeliveryPersons = deliveryPersonRepository.findAll();
        List<DeliveryPerson> filteredDeliveryPersons = new ArrayList<>();

        for (DeliveryPerson deliveryPerson : allDeliveryPersons) {
            // Filtrer par disponibilité (available)
            if (available != null) {
                if (available && !deliveryPerson.isAvailable()) {
                    continue; // Ignore les livreurs non disponibles
                } else if (!available && deliveryPerson.isAvailable()) {
                    continue; // Ignore les livreurs disponibles
                }
            }

            // Filtrer par date de création (creationDate)
            if (creationDate != null && !deliveryPerson.getCreationDate().isEqual(creationDate)) {
                continue; // Ignore les livreurs avec une date de création différente
            }

            // Filtrer par date de création après une date précise (creationDateAfter)
            if (creationDateAfter != null && deliveryPerson.getCreationDate().isBefore(creationDateAfter)) {
                continue; // Ignore les livreurs créés avant cette date
            }

            // Filtrer par date de création avant une date précise (creationDateBefore)
            if (creationDateBefore != null && deliveryPerson.getCreationDate().isAfter(creationDateBefore)) {
                continue; // Ignore les livreurs créés après cette date
            }

            // Filtrer par date de création entre deux dates précises (creationDateBetweenStart, creationDateBetweenEnd)
            if (creationDateBetweenStart != null && creationDateBetweenEnd != null) {
                if (deliveryPerson.getCreationDate().isBefore(creationDateBetweenStart)
                        || deliveryPerson.getCreationDate().isAfter(creationDateBetweenEnd)) {
                    continue; // Ignore les livreurs en dehors de la plage de dates
                }
            }
            filteredDeliveryPersons.add(deliveryPerson);
        }

        // Tri des résultats
        if ("name".equalsIgnoreCase(sortBy)) {
            filteredDeliveryPersons.sort(Comparator.comparing(DeliveryPerson::getName));
        } else if ("name_desc".equalsIgnoreCase(sortBy)) {
            filteredDeliveryPersons.sort(Comparator.comparing(DeliveryPerson::getName).reversed());
        } else if ("creationDate".equalsIgnoreCase(sortBy)) {
            filteredDeliveryPersons.sort(Comparator.comparing(DeliveryPerson::getCreationDate));
        } else if ("creationDate_desc".equalsIgnoreCase(sortBy)) {
            filteredDeliveryPersons.sort(Comparator.comparing(DeliveryPerson::getCreationDate).reversed());
        }

        return filteredDeliveryPersons;
    }

    public Page<DeliveryPersonDetailsDTO> getAllDeliveryPersonsWithCounts(Pageable pageable) {
        Page<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findAll(pageable);
        return deliveryPersons.map(this::mapToDeliveryPersonWithCountsDTO);
    }

    private DeliveryPersonDetailsDTO mapToDeliveryPersonWithCountsDTO(DeliveryPerson deliveryPerson) {
        int deliveryCount = deliveryPerson.getDeliveries() != null ? deliveryPerson.getDeliveries().size() : 0;
        int tourCount = deliveryPerson.getTours() != null ? deliveryPerson.getTours().size() : 0;

        // Création d'une instance DeliveryPerson sans tourCount
        DeliveryPerson deliveryPersonWithoutTourCount = new DeliveryPerson();
        deliveryPersonWithoutTourCount.setId(deliveryPerson.getId());
        deliveryPersonWithoutTourCount.setName(deliveryPerson.getName());
        deliveryPersonWithoutTourCount.setAvailable(deliveryPerson.isAvailable());
        deliveryPersonWithoutTourCount.setCreationDate(deliveryPerson.getCreationDate());
        deliveryPersonWithoutTourCount.setDeliveries(deliveryPerson.getDeliveries());
        deliveryPersonWithoutTourCount.setTours(deliveryPerson.getTours());

        // Création de l'objet DTO avec la DeliveryPerson temporaire sans tourCount
        DeliveryPersonDetailsDTO dto = new DeliveryPersonDetailsDTO(deliveryPersonWithoutTourCount, deliveryCount, tourCount);

        return dto;
    }

    //trier par le nombre de tournées
    public List<DeliveryPerson> trierLivreursParNombreTournées() {
        return deliveryPersonRepository.trierLivreursParNombreTournées();
    }

    public DeliveryPersonDetailsDTO getDeliveryPersonDetails(Long deliveryPersonId) {
        Optional<DeliveryPerson> deliveryPersonOptional = deliveryPersonRepository.findById(deliveryPersonId);
        if (deliveryPersonOptional.isPresent()) {
            DeliveryPerson deliveryPerson = deliveryPersonOptional.get();
            int deliveryCount = deliveryRepository.countDeliveriesByDeliveryPersonId(deliveryPersonId);
            int tourCount = tourRepository.countToursByDeliveryPersonsId(deliveryPersonId);

            return new DeliveryPersonDetailsDTO(deliveryPerson, deliveryCount,tourCount);
        }
        return null;
    }
    public DeliveryPerson findDeliveryPersonById(Long id) {
        return deliveryPersonRepository.findById(id).orElse(null);
    }

}














