package fr.TabetSaidi.tp1Backend.controllers;

import fr.TabetSaidi.tp1Backend.dto.TourDto;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.entity.Tour;
import fr.TabetSaidi.tp1Backend.service.TourService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tours")
@Api(tags = "Gestion des tournées", description = "Endpoints pour la gestion des tournées")
public class TourController {
    private final TourService tourService;
    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Obtenir les tours", notes = "Récupère la liste des tours")
    public List<Tour> getAllTours() {

        return tourService.getAllTour();
    }
    // Endpoint pour créer une tournée et l'assigner a un livreur
    @PostMapping("/create/{deliveryPersonId}")
    @ApiOperation(value = "Créer une tournée pour un livreur", notes = "Crée une nouvelle tournée et l'associe à un livreur")
    public ResponseEntity<?> createTour(@RequestBody TourDto tourDto, @PathVariable Long deliveryPersonId) {
        try {
            TourDto createdTour = tourService.createTourForDeliveryPerson(tourDto, deliveryPersonId);
            return new ResponseEntity<>(createdTour, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            // Capturer l'exception et renvoyer une réponse avec un message d'erreur approprié
            String errorMessage = "Ce livreur est déjà associé à une tournée en cours.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }
    // Endpoint pour mettre à jour une tournée existante
    @PutMapping("/{id}")
    @ApiOperation(value = "Mettre à jour une tournée", notes = "Met à jour une tournée existante en fonction de l'ID fourni")
    public ResponseEntity<TourDto> updateTour(@PathVariable Long id, @RequestBody TourDto updatedTourDto) {
        TourDto updatedTour = tourService.updateTour(id, updatedTourDto);
        if (updatedTour != null) {
            return ResponseEntity.ok(updatedTour);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour supprimer une tournée par ID
    @DeleteMapping("/{id}")
    @ApiOperation(value = "Supprimer une tournée par ID", notes = "Supprime une tournée en fonction de l'ID fourni")
    public ResponseEntity<String> deleteTour(@PathVariable Long id) {
        boolean deleted = tourService.deleteTourById(id);

        if (deleted) {
            return ResponseEntity.ok("Tour with ID " + id + " has been deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //E_40
    @PostMapping("/{tourId}/deliveries/add")
    @ApiOperation(value = "Ajouter des livraisons à une tournée", notes = "Ajoute des livraisons à une tournée spécifique")
    public ResponseEntity<String> addDeliveriesToTour(@PathVariable Long tourId, @RequestBody List<Long> deliveryIds) {
        boolean added = tourService.addDeliveriesToTour(tourId, deliveryIds);
        if (added) {
            return ResponseEntity.ok("Deliveries added to tour with ID " + tourId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //E50
    @GetMapping("/delivery/{deliveryPersonId}")
    @ApiOperation(value = "Obtenir les tournées par ID de livreur", notes = "Récupère les tournées associées à un ID de livreur spécifique")
    public ResponseEntity<Page<TourDto>> getToursByDeliveryPersonId(
            @PathVariable Long deliveryPersonId,
            Pageable pageable
    ) {
        Page<TourDto> tours = tourService.getToursByDeliveryPersonId(deliveryPersonId, pageable);
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }

    //E_60
    @GetMapping("/byDate")
    @ApiOperation(value = "Obtenir les tournées par date", notes = "Récupère les tournées en fonction d'une date spécifique")
    public ResponseEntity<Page<TourDto>> getToursByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable
    ) {
        Page<TourDto> tours = tourService.getToursByDate(date, pageable);
        return new ResponseEntity<>(tours, HttpStatus.OK);
    }

    //E_80
    // Endpoint pour obtenir une tournée par ID
    @GetMapping("/{tourId}")
    @ApiOperation(value = "Obtenir une tournée par ID", notes = "Récupère les détails d'une tournée en fonction de l'ID fourni")
    public ResponseEntity<TourDto> getTourById(@PathVariable Long tourId) {
        TourDto tourDetails = tourService.getTourDetailsById(tourId);
        if (tourDetails != null) {
            return new ResponseEntity<>(tourDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
