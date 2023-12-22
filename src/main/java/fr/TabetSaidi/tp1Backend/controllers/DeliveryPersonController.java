package fr.TabetSaidi.tp1Backend.controllers;
import fr.TabetSaidi.tp1Backend.dto.DeliveryPersonDetailsDTO;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
import fr.TabetSaidi.tp1Backend.service.DeliveryPersonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/deliverypersons")
@Api(tags = "Gestion des livreurs", description = "Endpoints pour la gestion des livreurs")
public class DeliveryPersonController {
    private final DeliveryPersonService deliveryPersonService;


    @Autowired
    public DeliveryPersonController(DeliveryPersonService deliveryPersonService) {
        this.deliveryPersonService = deliveryPersonService;
    }
    @PostMapping
    @ApiOperation(value = "Ajouter un livreur", notes = "Ajoute un nouveau livreur avec les détails fournis")
    public ResponseEntity<DeliveryPersonDetailsDTO> addDeliveryPerson(@RequestBody DeliveryPerson deliveryPerson) {
        return ResponseEntity.ok(deliveryPersonService.createDeliveryPerson(deliveryPerson));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Obtenir tous les livreurs", notes = "Récupère la liste de tous les livreurs")
    public List<DeliveryPerson> getAllDeliveryPersons() {

        return deliveryPersonService.getAllDeliveryPersons();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtenir un livreur par ID", notes = "Récupère les détails d'un livreur en fonction de l'ID fourni")
    public ResponseEntity<DeliveryPerson> getDeliveryPersonById(@PathVariable Long id) {
        DeliveryPerson deliveryPerson = deliveryPersonService.getDeliveryPersonById(id);

        if (deliveryPerson != null) {
            return ResponseEntity.ok(deliveryPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Supprimer un livreur par ID", notes = "Supprime un livreur en fonction de l'ID fourni")
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
    @ApiOperation(value = "Mettre à jour un livreur existant", notes = "Modifie les détails d'un livreur existant en fonction de l'ID fourni")
    public ResponseEntity<DeliveryPerson> updateDeliveryPerson(@PathVariable Long id, @RequestBody DeliveryPerson updatedDeliveryPerson) {
        DeliveryPerson updatedPerson = deliveryPersonService.updateDeliveryPerson(id, updatedDeliveryPerson);
        return ResponseEntity.ok(updatedPerson);
    }

    //Recherche paginée des livreurs
    @GetMapping("/page")
    @ApiOperation(value = "Obtenir les livreurs de manière paginée", notes = "Récupère les livreurs de manière paginée avec pagination et taille spécifiées")
    public ResponseEntity<Page<DeliveryPerson>> getAllDeliveryPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DeliveryPerson> deliveryPersons = deliveryPersonService.getAllDeliveryPersonsPage(PageRequest.of(page, size));
        return ResponseEntity.ok(deliveryPersons);
    }

    //Recherche avec filtres
    @GetMapping("/search")
    @ApiOperation(value = "Rechercher des livreurs avec filtres", notes = "Récupère les livreurs en fonction des filtres spécifiés")
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

    // Endpoint pour la recherche paginée avec les comptes de livraisons et tournées
    @GetMapping("/paged-with-counts")
    @ApiOperation(value = "Obtenir les livreurs de manière paginée avec comptes", notes = "Récupère les livreurs de manière paginée avec comptes de livraisons et tournées")
    public ResponseEntity<Page<DeliveryPersonDetailsDTO>> getAllDeliveryPersonsWithCounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<DeliveryPersonDetailsDTO> deliveryPersons =
                deliveryPersonService.getAllDeliveryPersonsWithCounts(PageRequest.of(page, size));
        return ResponseEntity.ok(deliveryPersons);
    }

    // Endpoint pour obtenir les livreurs triés par nombre de tournées
    @ApiOperation(value = "Obtenir les livreurs triés par nombre de tournées", notes = "Récupère les livreurs triés par nombre de tournées")
    @GetMapping("/sortedbytours")
    public ResponseEntity<List<DeliveryPerson>> getDeliveryPersonsSortedByTours() {
        List<DeliveryPerson> sortedDeliveryPersons = deliveryPersonService.trierLivreursParNombreTournées();
        return ResponseEntity.ok(sortedDeliveryPersons);
    }

    //avec details Livreurs
    @GetMapping("/details/{id}")
    @ApiOperation(value = "Obtenir les détails d'un livreur", notes = "Récupère les détails d'un livreur avec ses tournées et livraisons associées")
        public ResponseEntity<Map<String, Object>> getDeliveryPersonDetails(@PathVariable Long id) {
            Map<String, Object> response = new HashMap<>();

            DeliveryPerson deliveryPerson = deliveryPersonService.findDeliveryPersonById(id);

            if (deliveryPerson != null) {
                response.put("deliveryPerson", deliveryPerson);
                response.put("tourCount", deliveryPerson.getTours() != null ? deliveryPerson.getTours().size() : 0);
                response.put("deliveryCount", deliveryPerson.getDeliveries() != null ? deliveryPerson.getDeliveries().size() : 0);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }






















