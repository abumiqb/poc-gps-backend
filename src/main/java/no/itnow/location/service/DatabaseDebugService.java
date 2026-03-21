package no.itnow.location.service;

import no.itnow.location.model.DatabaseInfoResponse;
import no.itnow.location.model.DatabaseTableInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseDebugService {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final String jdbcUrl;

    public DatabaseDebugService(JdbcTemplate jdbcTemplate,
                                DataSource dataSource,
                                @Value("${spring.datasource.url}") String jdbcUrl) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.jdbcUrl = jdbcUrl;
    }

    public DatabaseInfoResponse getDatabaseInfo() {
        List<String> tableNames = jdbcTemplate.queryForList("""
                SELECT TABLE_NAME
                FROM INFORMATION_SCHEMA.TABLES
                WHERE TABLE_SCHEMA = 'PUBLIC'
                ORDER BY TABLE_NAME
                """, String.class);

        List<DatabaseTableInfoResponse> tables = new ArrayList<>();
        for (String tableName : tableNames) {
            Long rowCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM " + tableName,
                    Long.class
            );
            tables.add(new DatabaseTableInfoResponse(tableName, rowCount == null ? 0 : rowCount));
        }

        return new DatabaseInfoResponse(
                jdbcUrl,
                getDatabaseProductName(),
                tables
        );
    }

    private String getDatabaseProductName() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getDatabaseProductName();
        } catch (SQLException exception) {
            return "unknown";
        }
    }
}
