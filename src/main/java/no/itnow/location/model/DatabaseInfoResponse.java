package no.itnow.location.model;

import java.util.List;

public record DatabaseInfoResponse(
        String jdbcUrl,
        String databaseProductName,
        List<DatabaseTableInfoResponse> tables
) {
}
