package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDateTime;

public class AppTest {

    private static Javalin app;

    @BeforeEach
    public void setUp() throws Exception {
        // Удаляем таблицу перед каждым тестом
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:hexlet");
             Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS urls");
        }
        app = App.getApp();
    }

    @Test
    void testIndexPage() throws Exception {

        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlPage() throws Exception {
        String name = "http://www.new.ru";
        LocalDateTime createdAt = LocalDateTime.now();
        var url = new Url(name, createdAt);
        UrlsRepository.save(url);
        url.setId(1L);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreateUrl() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "url=https://www.example.com");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    void testUrlsPage() throws Exception {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }
}
