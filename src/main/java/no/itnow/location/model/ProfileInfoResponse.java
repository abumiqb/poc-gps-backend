package no.itnow.location.model;

import java.util.List;

public record ProfileInfoResponse(
        String applicationName,
        String environmentName,
        List<String> activeProfiles,
        String backendBaseUrl,
        String frontendBaseUrl,
        String h2ConsoleUrl,
        String h2JdbcUrl
) {
}
