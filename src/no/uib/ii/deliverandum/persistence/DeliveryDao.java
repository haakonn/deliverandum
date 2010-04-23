package no.uib.ii.deliverandum.persistence;

import java.util.List;

import lombok.NonNull;

import no.uib.ii.deliverandum.beans.Delivery;

public interface DeliveryDao {

    public Delivery persist(@NonNull Delivery delivery);
    
    public Delivery getDelivery(@NonNull int deliveryId);
    
    public List<Delivery> getDeliveries(@NonNull String course, Integer assignmentId, String deliveredById, String assignedTo);
    
}
