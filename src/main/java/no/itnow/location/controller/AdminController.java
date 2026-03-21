package no.itnow.location.controller;

import lombok.RequiredArgsConstructor;
import no.itnow.location.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final SessionService sessionService;

    // Kun for utvikling: tømmer alle turer og posisjonsdata uten å restarte appen.
    @PostMapping("/reset")
    public ResponseEntity<Void> reset() {
        sessionService.reset();
        return ResponseEntity.noContent().build();
    }
}
