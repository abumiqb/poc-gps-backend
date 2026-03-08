package no.itnow.location.service;

import no.itnow.location.model.LocationRequest;
import no.itnow.location.model.LocationResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class LocationService {

    private final AtomicReference<LocationResponse> latestLocation = new AtomicReference<>();

    public LocationResponse save(LocationRequest request) {
        LocationResponse response = new LocationResponse(
                request.latitude(),
                request.longitude(),
                Instant.now()
        );
        latestLocation.set(response);
        return response;
    }

    public LocationResponse getLatest() {
        return latestLocation.get();
    }
}