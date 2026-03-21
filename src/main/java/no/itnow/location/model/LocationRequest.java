package no.itnow.location.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LocationRequest(
    @NotNull
    @Size(min = 1, max = 100)
    String sessionId,

    @NotNull
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    Double latitude,

    @NotNull
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    Double longitude,

    @DecimalMin(value = "0.0")
    Double speed,

    @DecimalMin(value = "0.0")
    @DecimalMax(value = "360.0")
    Double heading
) {
}
