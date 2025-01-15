package com.devcrew.logmicroservice.config;

import com.devcrew.logmicroservice.model.*;
import com.devcrew.logmicroservice.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to populate the database with initial data.
 * It is used to populate the database with the list of actions, modules, and tables.
 * The list of actions and modules are predefined, while the list of tables is retrieved from the database.
 */
@Configuration
// Enable Spring Data Web Support for Pageable and Sort objects
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class InitialDataConfig {

    /**
     * JDBC URL, username, and password are used to connect to the database to retrieve the list of tables.
     */
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    /**
     * This method is used to populate the database with the list of actions, modules, and tables.
     * @param actionRepository The repository for the Action entity
     * @param appEntityRepository The repository for the AppEntity entity
     * @param moduleRepository The repository for the Module entity
     * @return CommandLineRunner object that is used to populate the database with the list of actions, modules, and tables
     */
    // Just to test the application
    @Bean
    CommandLineRunner commandLineRunner(ActionRepository actionRepository, AppEntityRepository appEntityRepository, ModuleRepository moduleRepository) {
        return args -> {
            List<Action> actions = new ArrayList<>(
                    List.of(
                            new Action(1, "Create"),
                            new Action(2, "Read"),
                            new Action(3, "Update"),
                            new Action(4, "Delete")
                    )
            );
            actionRepository.saveAll(actions);
            List<AppModule> modules = new ArrayList<>(
                    List.of(
                            new AppModule(1, "User"),
                            new AppModule(2, "Role"),
                            new AppModule(3, "Permission"),
                            new AppModule(4, "Log"),
                            new AppModule(5, "Entity"),
                            new AppModule(6, "Module"),
                            new AppModule(7, "Action"),
                            new AppModule(8, "Customer"),
                            new AppModule(9, "Product"),
                            new AppModule(10, "Order"),
                            new AppModule(11, "ProductDetail"),
                            new AppModule(12, "OrderDetail"),
                            new AppModule(13, "Payment")
                    )
            );
            moduleRepository.saveAll(modules);
            appEntityRepository.saveAll(getTables());
        };
    }

    /**
     * This method is used to retrieve the list of tables from the database.
     * @return List of AppEntity objects that represent the tables in the database
     */
    private List<AppEntity> getTables() {
        List<AppEntity> tablesList = new ArrayList<>();
        int tableIDcount = 1;
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                // Change condition to exclude tables that are not needed, e.g., system tables; change if needed
                if (!(tableName.contains("trace") || tableName.contains("flyway") || tableName.contains("schema") || tableName.contains("hibernate"))) {
                    tablesList.add(new AppEntity(tableIDcount++, tableName));
                }
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return tablesList;
    }
}
