package no.itnow.location.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import no.itnow.location.model.SessionCreateRequest;
import no.itnow.location.model.SessionResponse;
import no.itnow.location.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sessions")
@CrossOrigin(origins = "*")
public class SessionController {

    private final SessionService sessionService;

    // Starter en ny aktiv tur og returnerer sessionId som frontend skal bruke videre.
    @PostMapping
    public ResponseEntity<SessionResponse> createSession(@Valid @RequestBody SessionCreateRequest request) {
        return ResponseEntity.ok(sessionService.create(request));
    }

    // Lar admin eller kartklient se hvilke turer som fortsatt er aktive.
    @GetMapping("/active")
    public ResponseEntity<List<SessionResponse>> getActiveSessions() {
        List<SessionResponse> sessions = sessionService.getActiveSessions();
        if (sessions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(sessions);
    }

    // Avslutter turen og fjerner live-markøren fra siste-posisjon-tabellen.
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<SessionResponse> endSession(@PathVariable String sessionId) {
        return ResponseEntity.ok(sessionService.endSession(sessionId));
    }
}
