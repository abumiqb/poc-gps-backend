package no.itnow.location.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.itnow.location.model.LocationRequest;
import no.itnow.location.model.LocationResponse;
import no.itnow.location.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
@CrossOrigin(origins = "*")
public class LocationController {

    private final LocationService locationService;
    private final SimpMessagingTemplate messagingTemplate;

    // Lagrer siste posisjon for en tur og sender oppdateringen videre til kartklienter.
    @PostMapping("/update")
    public ResponseEntity<LocationResponse> updateLocation(
            @Valid @RequestBody LocationRequest request
    ) {
        LocationResponse saved = locationService.save(request);

        messagingTemplate.convertAndSend("/topic/location", saved);

        return ResponseEntity.ok(saved);
    }

    // Returnerer siste kjente posisjon for alle aktive turer som har sendt inn lokasjon.
    @GetMapping("/latest")
    public ResponseEntity<List<LocationResponse>> getLatestLocations() {
        List<LocationResponse> latest = locationService.getLatest();
        if (latest.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latest);
    }

    // Returnerer siste kjente posisjon for én bestemt tur.
    @GetMapping("/latest/{sessionId}")
    public ResponseEntity<LocationResponse> getLatestLocationBySessionId(@PathVariable String sessionId) {
        LocationResponse latest = locationService.getLatestBySessionId(sessionId);
        if (latest == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latest);
    }

    // Returnerer hele ruten som er lagret for én bestemt tur.
    @GetMapping("/history/{sessionId}")
    public ResponseEntity<List<LocationResponse>> getHistoryBySessionId(@PathVariable String sessionId) {
        List<LocationResponse> history = locationService.getHistoryBySessionId(sessionId);
        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(history);
    }
}
