package fr.TabetSaidi.tp1Backend.dto;
import fr.TabetSaidi.tp1Backend.entity.Delivery;
import lombok.*;


import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDto {
    private Long id;
    private String addressFrom;
    private String addressTo;
    private LocalDate deliveryDate;

    public static DeliveryDto fromEntity(Delivery delivery) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setId(delivery.getId());
        deliveryDto.setAddressFrom(delivery.getAddressFrom());
        deliveryDto.setAddressTo(delivery.getAddressTo());
        deliveryDto.setDeliveryDate(delivery.getDeliveryDate());
        return deliveryDto;
    }

    public Delivery toEntity() {
        Delivery delivery = new Delivery();
        delivery.setId(this.getId());
        delivery.setAddressFrom(this.getAddressFrom());
        delivery.setAddressTo(this.getAddressTo());
        delivery.setDeliveryDate(this.getDeliveryDate());
        return delivery;
    }
}