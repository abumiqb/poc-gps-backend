package no.itnow.location.controller;

import jakarta.validation.Valid;
import no.itnow.location.model.LocationRequest;
import no.itnow.location.model.LocationResponse;
import no.itnow.location.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;
    private final SimpMessagingTemplate messagingTemplate;

    public LocationController(LocationService locationService,
                              SimpMessagingTemplate messagingTemplate) {
        this.locationService = locationService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/update")
    public ResponseEntity<LocationResponse> updateLocation(
            @Valid @RequestBody LocationRequest request
    ) {
        LocationResponse saved = locationService.save(request);

        messagingTemplate.convertAndSend("/topic/location", saved);

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/latest")
    public ResponseEntity<LocationResponse> getLatestLocation() {
        LocationResponse latest = locationService.getLatest();
        if (latest == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latest);
    }
}