package fr.TabetSaidi.tp1Backend.controllers;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.service.DeliveryPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/deliverypersons")
public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;

    @Autowired
    public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
        this.deliveryPersonService = deliveryPersonService;
    }
    @PostMapping
    public ResponseEntity<DeliveryPerson> addDeliveryPerson(@RequestBody DeliveryPerson deliveryPerson) {
      return ResponseEntity.ok(deliveryPersonService.createDeliveryPerson(deliveryPerson));

    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeliveryPerson> getAllDeliveryPersons() {

        return deliveryPersonService.getAllDeliveryPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryPerson> getDeliveryPersonById(@PathVariable Long id) {
        DeliveryPerson deliveryPerson = deliveryPersonService.getDeliveryPersonById(id);

        if (deliveryPerson != null) {
            return ResponseEntity.ok(deliveryPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDeliveryPerson(@PathVariable Long id) {
        DeliveryPerson deliveryPerson = deliveryPersonService.getDeliveryPersonById(id);

        if (deliveryPerson != null) {
            deliveryPersonService.deleteDeliveryPerson(id);
            String message = "Delivery with ID : " + id + " deleted.";
            return ResponseEntity.ok(message);
        } else {
            String errorMessage = "Delivery with ID : " + id + " not found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

        //Modification d'un livreur existant
        @PutMapping("/{id}")
        public ResponseEntity<DeliveryPerson> updateDeliveryPerson(@PathVariable Long id, @RequestBody DeliveryPerson updatedDeliveryPerson) {
            DeliveryPerson updatedPerson = deliveryPersonService.updateDeliveryPerson(id, updatedDeliveryPerson);
            return ResponseEntity.ok(updatedPerson);
        }

        //Recherche paginée des livreurs
        @GetMapping("/page")
        public ResponseEntity<Page<DeliveryPerson>> getAllDeliveryPersons(
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size) {
            Page<DeliveryPerson> deliveryPersons = deliveryPersonService.getAllDeliveryPersonsPage(PageRequest.of(page, size));
            return ResponseEntity.ok(deliveryPersons);
        }

        //Recherche avec filtres
        @GetMapping("/search")
        public ResponseEntity<List<DeliveryPerson>> searchDeliveryPersons(
                @RequestParam(required = false) Boolean available,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDate,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDateAfter,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDateBefore,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDateBetweenStart,
                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate creationDateBetweenEnd,
                @RequestParam(required = false) String sortBy) {
            List<DeliveryPerson> filteredDeliveryPersons = deliveryPersonService.searchDeliveryPersons(
                    available, creationDate, creationDateAfter, creationDateBefore, creationDateBetweenStart, creationDateBetweenEnd, sortBy);
            return ResponseEntity.ok(filteredDeliveryPersons);
        }
    }













