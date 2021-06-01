package at.fhtw.repository;

import at.fhtw.properties.DBProperties;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionFactory {

    private final DBProperties dbProperties;

    public ConnectionFactory(DBProperties dbProperties) {
        this.dbProperties = dbProperties;

        Flyway flyway = Flyway.configure().dataSource(dbProperties.getUrl(), dbProperties.getUser(), dbProperties.getPass()).load();
        flyway.migrate();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbProperties.getUrl(), dbProperties.getUser(), dbProperties.getPass());
    }
}
