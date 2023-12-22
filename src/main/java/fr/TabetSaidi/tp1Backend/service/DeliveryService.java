package fr.TabetSaidi.tp1Backend.service;

import fr.TabetSaidi.tp1Backend.dto.DeliveryDto;
import fr.TabetSaidi.tp1Backend.entity.Delivery;
import fr.TabetSaidi.tp1Backend.entity.Tour;
import fr.TabetSaidi.tp1Backend.repository.DeliveryRepository;
import fr.TabetSaidi.tp1Backend.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final TourRepository tourRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository, TourRepository tourRepository) {
        this.deliveryRepository = deliveryRepository;
        this.tourRepository = tourRepository;
    }

    //créer une livraison
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        // Convertir le DTO en entité
        Delivery delivery = deliveryDto.toEntity();

        // Ajouter les validations nécessaires, par exemple, vérifier si les adresses sont renseignées
        if (delivery.getAddressFrom() == null || delivery.getAddressTo() == null) {
            // Gérer l'erreur, par exemple, lancer une exception
            throw new IllegalArgumentException("L'adresse de ramassage et l'adresse de dépôt sont obligatoires.");
        }

        // Ajouter ici la logique pour enregistrer la livraison en base de données (en utilisant le repository approprié)
        delivery = deliveryRepository.save(delivery); // Enregistrement de la livraison dans la base de données

        // Convertir l'entité mise à jour en DTO
        DeliveryDto createdDeliveryDto = DeliveryDto.fromEntity(delivery);

        // Affecter l'ID nouvellement généré au DTO
        createdDeliveryDto.setId(delivery.getId());

        // Retourner le DTO avec l'ID
        return createdDeliveryDto;
    }


    public DeliveryDto getDeliveryById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(NoSuchElementException ::new);
        return DeliveryDto.fromEntity(delivery);
    }

    public DeliveryDto updateDeliveryField(Long deliveryId, String field, String value) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(NoSuchElementException::new);

        switch (field) {
            case "addressFrom":
                delivery.setAddressFrom(value);
                break;
            case "addressTo":
                delivery.setAddressTo(value);
                break;
            case "deliveryDate":
                LocalDate newDate = LocalDate.parse(value); // Parse la nouvelle date
                delivery.setDeliveryDate(newDate);
                break;
            // Ajoutez d'autres cas pour d'autres champs que vous souhaitez modifier
            default:
                throw new IllegalArgumentException("Le champ spécifié n'est pas valide pour la livraison.");
        }

        Delivery updatedDelivery = deliveryRepository.save(delivery);
        return DeliveryDto.fromEntity(updatedDelivery);
    }
    public void deleteDelivery(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(NoSuchElementException::new);

        // Supprimer la livraison à partir du repository
        deliveryRepository.delete(delivery);
    }
   public Page<Delivery> getAllDeliveriesPaged(Pageable pageable) {
        return deliveryRepository.findAll(pageable);
    }
    public void createOrUpdateDelivery(Delivery delivery, Long tourId) throws ChangeSetPersister.NotFoundException {
        // Récupérer la Tour correspondant à l'identifiant tourId
        Tour tour = tourRepository.findById(tourId).orElseThrow(ChangeSetPersister.NotFoundException::new);

        // Associer la Delivery à la Tour récupérée
        delivery.setTour(tour);

        // Enregistrer ou mettre à jour la Delivery dans la base de données
        deliveryRepository.save(delivery);
    }

    public DeliveryDto getDeliveryDetails(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
        if (delivery != null) {
            return DeliveryDto.fromEntity(delivery);
        }
        return null;
    }

}



