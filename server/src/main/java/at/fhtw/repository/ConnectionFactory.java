package at.fhtw.repository;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private String dbUser = "user";
    private String dbPass = "pass";
    private String connectionUrl = "jdbc:postgresql://localhost/tourplanner";

    public ConnectionFactory() {
        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://localhost/tourplanner", dbUser, dbPass).load();
        flyway.migrate();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl, dbUser, dbPass);
    }
}
