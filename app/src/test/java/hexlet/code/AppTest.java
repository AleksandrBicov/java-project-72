package hexlet.code;

import static org.assertj.core.api.Assertions.assertThat;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class AppTest {

    private static Javalin app;
    private static MockWebServer server;

    private static String readResourceFile(String fileName) throws IOException {
        try (InputStream inputStream = AppTest.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + fileName);
            }
            return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
        }
    }

    @BeforeAll
    public static void startMock() throws IOException {
        server = new MockWebServer();
        MockResponse response = new MockResponse()
                .setBody(readResourceFile("MockWebServer.html")).setResponseCode(200);
        server.enqueue(response);
        server.start();
    }

    @AfterAll
    public static void stopMockServer() throws IOException {
        server.shutdown();
    }

    @BeforeEach
    public final void beforeEach() throws IOException, SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:hexlet");
             Statement statement = connection.createStatement()) {
            String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/init.sql")));
            statement.execute(sql);
        }
        app = App.getApp();
    }



    @AfterEach
    public final void afterEach() {
        if (app != null) {
            app.stop();
        }
    }

    @Test
    public void indexTest() {
        JavalinTest.test(app, (ignored, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void urlListTest() {
        JavalinTest.test(app, (ignored, client) -> {
            var response = client.get("/urls");
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void notFoundUrlTest() throws SQLException {
        JavalinTest.test(app, (ignored, client) -> {
            var response = client.get("/urls/100");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void searchUrlTest() throws SQLException {
        var url = new Url("https://www.example.com");
        UrlsRepository.save(url);
        JavalinTest.test(app, (ignored, client) -> {
            var response = client.get("/urls/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
        });
    }

    @Test
    public void checkTest() {
        String serverUrl = server.url("/").toString();
        JavalinTest.test(app, (ignored, client) -> {
            Url url = new Url(serverUrl);
            UrlsRepository.save(url);

            client.post("/urls/" + url.getId() + "/checks");

            List<UrlCheck> checkUrl = UrlCheckRepository.find(url.getId());

            String title = checkUrl.get(0).getTitle();
            String h1 = checkUrl.get(0).getH1();
            String description = checkUrl.get(0).getDescription();
            assertThat(title).isEqualTo("hexlet");
            assertThat(h1).isEqualTo("Hello World");
            assertThat(description).isEqualTo("hexlet web server");
        });
    }
}
