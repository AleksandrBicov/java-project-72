package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import hexlet.code.Controller.Controller;
import hexlet.code.repository.BaseRepository;

import io.javalin.Javalin;

import io.javalin.rendering.template.JavalinJte;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.sql.SQLException;
import java.util.stream.Collectors;

import static hexlet.code.config.TemplateEngineConfig.createTemplateEngine;

@Slf4j
public class App {

    public static Javalin getApp() throws SQLException {

        var hikariConfig = new HikariConfig();

        String jdbcDatabaseUrl = System.getenv("JDBC_DATABASE_URL");

        if (jdbcDatabaseUrl == null || jdbcDatabaseUrl.isEmpty()) {
            // Локальная разработка: используем H2 в памяти
            jdbcDatabaseUrl = "jdbc:h2:mem:hexlet;DB_CLOSE_DELAY=-1;";
        }

        hikariConfig.setJdbcUrl(jdbcDatabaseUrl);

        var dataSource = new HikariDataSource(hikariConfig);
        var url = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", Controller::index);

        return app;
    }


    public static void main(String[] args) throws SQLException {
        Javalin app = getApp();
        app.start(7070);
    }
}