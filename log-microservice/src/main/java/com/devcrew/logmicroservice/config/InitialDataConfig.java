package com.devcrew.logmicroservice.config;

import com.devcrew.logmicroservice.model.Action;
import com.devcrew.logmicroservice.model.AppEntity;
import com.devcrew.logmicroservice.model.AppModule;
import com.devcrew.logmicroservice.repository.ActionRepository;
import com.devcrew.logmicroservice.repository.AppEntityRepository;
import com.devcrew.logmicroservice.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class InitialDataConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

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
