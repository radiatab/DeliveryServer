package fr.TabetSaidi.tp1Backend.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;

import java.util.HashMap;
import java.util.Map;
import fr.TabetSaidi.tp1Backend.entity.DeliveryPerson;
public class DeliveryPersonDetailsDTO {
    private DeliveryPerson deliveryPerson;
    private int deliveryCount;
    private int tourCount;

    public DeliveryPersonDetailsDTO() {
    }

    public DeliveryPersonDetailsDTO(DeliveryPerson deliveryPerson, int deliveryCount, int tourCount) {
        this.deliveryPerson = deliveryPerson;
        this.deliveryCount = deliveryCount;
        this.tourCount = tourCount;
    }

    public DeliveryPerson getDeliveryPerson() {
        return deliveryPerson;
    }

    public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    public int getDeliveryCount() {
        return deliveryCount;
    }

    public void setDeliveryCount(int deliveryCount) {
        this.deliveryCount = deliveryCount;
    }

    public int getTourCount() {
        return tourCount;
    }

    public void setTourCount(int tourCount) {
        this.tourCount = tourCount;
    }



}

