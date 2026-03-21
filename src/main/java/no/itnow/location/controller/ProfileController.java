package no.itnow.location.controller;

import no.itnow.location.model.ProfileInfoResponse;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class ProfileController {

    private final Environment environment;

    public ProfileController(Environment environment) {
        this.environment = environment;
    }

    // Lar frontend sjekke hvilket miljø og hvilke profiler backend kjører med.
    @GetMapping("/profile")
    public ProfileInfoResponse getProfileInfo() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        String environmentName = environment.getProperty("app.environment.name", "unknown");
        String applicationName = environment.getProperty("spring.application.name", "gps-backend");
        String backendBaseUrl = environment.getProperty("app.environment.backend-base-url", "");
        String frontendBaseUrl = environment.getProperty("app.environment.frontend-base-url", "");
        String h2ConsoleUrl = environment.getProperty("app.environment.h2-console-url", "");
        String h2JdbcUrl = environment.getProperty("app.environment.h2-jdbc-url", "");

        return new ProfileInfoResponse(
                applicationName,
                environmentName,
                activeProfiles,
                backendBaseUrl,
                frontendBaseUrl,
                h2ConsoleUrl,
                h2JdbcUrl
        );
    }
}
