package no.itnow.location.controller;

import lombok.RequiredArgsConstructor;
import no.itnow.location.model.DatabaseInfoResponse;
import no.itnow.location.service.DatabaseDebugService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class DatabaseDebugController {

    private final DatabaseDebugService databaseDebugService;

    // Kun for utvikling: viser hvilke tabeller backend faktisk ser i databasen akkurat nå.
    @GetMapping("/db-info")
    public DatabaseInfoResponse getDatabaseInfo() {
        return databaseDebugService.getDatabaseInfo();
    }
}
