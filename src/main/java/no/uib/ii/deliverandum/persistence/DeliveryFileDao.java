package no.uib.ii.deliverandum.persistence;

import java.io.InputStream;

import lombok.NonNull;

import no.uib.ii.deliverandum.beans.Delivery;

public interface DeliveryFileDao {
    
    public void addFiles(@NonNull Delivery delivery);
    
    public void persist(
            @NonNull InputStream in,
            @NonNull String suggestedFilename,
            String notes,
            @NonNull Delivery delivery);
    
}
