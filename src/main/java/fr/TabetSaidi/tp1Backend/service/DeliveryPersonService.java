package fr.TabetSaidi.tp1Backend.service;

import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.repository.DeliveryPersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    @Autowired
    public DeliveryPersonService(DeliveryPersonRepository deliveryPersonRepository) {
        this.deliveryPersonRepository = deliveryPersonRepository;
    }
    public DeliveryPerson createDeliveryPerson(DeliveryPerson deliveryPerson) {
        deliveryPerson.setCreationDate(LocalDate.now());
        return deliveryPersonRepository.save(deliveryPerson);
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
            // Appliquez vos filtres ici en fonction des paramètres facultatifs

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

            // Si nous arrivons ici, le livreur a passé tous les filtres, ajoutez-le à la liste
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








}
