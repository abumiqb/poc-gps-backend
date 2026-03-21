package no.itnow.location.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SessionCreateRequest(
        @NotNull
        @Size(min = 1, max = 100)
        String displayName,

        @Size(max = 100)
        String deviceId
) {
}
