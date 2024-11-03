package com.devcrew.usermicroservice.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Test class for the database connection.
 */
@ActiveProfiles("test")
@SpringBootTest
public class DatabaseConnectionTest {

    /**
     * The data source to be tested.
     */
    @Autowired
    private DataSource dataSource;

    /**
     * Test the connection to the database.
     * The test gets a connection from the data source and prints the database name and version.
     * The test checks if the connection is not null.
     * @throws SQLException if the connection to the database fails.
     */
    @Test
    public void testConnection() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseProductName = metaData.getDatabaseProductName();
            String databaseProductVersion = metaData.getDatabaseProductVersion();
            String databaseName = connection.getCatalog();
            System.out.println("Connected to: " + databaseProductName + " version: " + databaseProductVersion);
            System.out.println("Database name: " + databaseName);
        }
    }

    /**
     * Test the selection of tables from the database.
     * The test gets a connection from the data source and prints the names of the tables in the database.
     * @throws SQLException if the connection to the database fails.
     */
    @Test
    public void testSelectTables() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("Table: " + tableName);
                }
            }
        }
    }

    /**
     * Test the selection of columns from the database.
     * The test gets a connection from the data source and prints the names and types of the columns in the tables.
     * @throws SQLException if the connection to the database fails.
     */
    @Test
    public void testSelectTableInformation() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");
                    System.out.println("Table: " + tableName);
                    try (ResultSet columns = metaData.getColumns(null, null, tableName, "%")) {
                        while (columns.next()) {
                            String columnName = columns.getString("COLUMN_NAME");
                            String columnType = columns.getString("TYPE_NAME");
                            System.out.println("Column: " + columnName + " Type: " + columnType);
                        }
                    }
                }
            }
        }
    }
}