package no.itnow.location.model;

public record DatabaseTableInfoResponse(
        String tableName,
        long rowCount
) {
}
