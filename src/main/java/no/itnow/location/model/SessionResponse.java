package no.itnow.location.model;

import java.time.Instant;

public record SessionResponse(
        Long id,
        String sessionId,
        String displayName,
        String deviceId,
        boolean active,
        Instant startedAt,
        Instant lastSeenAt,
        Instant endedAt
) {
}
