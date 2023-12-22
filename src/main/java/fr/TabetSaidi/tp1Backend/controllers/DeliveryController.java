package fr.TabetSaidi.tp1Backend.controllers;
import fr.TabetSaidi.tp1Backend.dto.DeliveryDto;
import fr.TabetSaidi.tp1Backend.entity.Delivery;
import fr.TabetSaidi.tp1Backend.service.DeliveryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/deliveries")
@Api(tags = "Gestion des livraisons", description = "Endpoints pour la gestion des livraisons")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/create")
    @ApiOperation(value = "Créer une livraison", notes = "Crée une nouvelle livraison avec les détails fournis")
    public ResponseEntity<Map<String, Object>> createDelivery(@RequestBody DeliveryDto deliveryDto) {
        try {
            DeliveryDto createdDelivery = deliveryService.createDelivery(deliveryDto);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "La livraison a été créée avec succès");
            response.put("delivery", createdDelivery);
            response.put("deliveryId", createdDelivery.getId());

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{deliveryId}")
    @ApiOperation(value = "Obtenir une livraison par ID", notes = "Récupère les détails d'une livraison en fonction de l'ID fourni")
    public ResponseEntity<DeliveryDto> getDeliveryById(@PathVariable Long deliveryId) {
        try {
            DeliveryDto delivery = deliveryService.getDeliveryById(deliveryId);
            return new ResponseEntity<>(delivery, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{deliveryId}")
    @ApiOperation(value = "Mettre à jour un champ de livraison", notes = "Met à jour un champ spécifique d'une livraison en fonction de l'ID fourni")
    public ResponseEntity<DeliveryDto> updateDeliveryField(
            @PathVariable Long deliveryId,
            @RequestBody Map<String, String> request) {
        String field = request.get("field");
        String value = request.get("value");

        try {
            DeliveryDto updatedDelivery = deliveryService.updateDeliveryField(deliveryId, field, value);
            return new ResponseEntity<>(updatedDelivery, HttpStatus.OK);
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{deliveryId}")
    @ApiOperation(value = "Supprimer une livraison par ID", notes = "Supprime une livraison en fonction de l'ID fourni")
    public ResponseEntity<String> deleteDelivery(@PathVariable Long deliveryId) {
        try {
            deliveryService.deleteDelivery(deliveryId);
            String message = "La livraison avec l'ID " + deliveryId + " a été supprimée avec succès.";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("La livraison spécifiée n'a pas été trouvée.", HttpStatus.NOT_FOUND);
        }
    }
   @GetMapping("/paged")
   @ApiOperation(value = "Obtenir les livraisons paginées", notes = "Récupère une liste paginée de livraisons")
    public Page<Delivery> getAllDeliveriesPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deliveryService.getAllDeliveriesPaged(pageable);
    }





}



