package no.itnow.location.model;

import java.time.Instant;

public record LocationResponse (
        Double latitude,
        Double longitude,
        Instant updatedAt
) {
}