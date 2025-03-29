package app.deliveryLocation.servise;

import app.deliveryLocation.model.DeliveryLocation;
import app.deliveryLocation.repository.DeliveryLocationRepository;

import app.user.model.User;

import app.web.dto.LocationEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeliveryLocationService {

    private final DeliveryLocationRepository pickupLocationRepository;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DeliveryLocationService(DeliveryLocationRepository pickupLocationRepository, PasswordEncoder passwordEncoder) {
        this.pickupLocationRepository = pickupLocationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DeliveryLocation edit(LocationEditRequest locationEditRequest, User user) {

        DeliveryLocation pickupLocation = DeliveryLocation.builder()

                .location(locationEditRequest.getLocation())
                .town(locationEditRequest.getTown())
                .owner(user)
                .build();

        // Записване на артикула в базата данни
        pickupLocation = pickupLocationRepository.save(pickupLocation);

        return pickupLocation;
    }

    public List<DeliveryLocation> getAllByOwnerId(UUID ownerId) {
        return pickupLocationRepository.findByOwnerId(ownerId);
    }
}
