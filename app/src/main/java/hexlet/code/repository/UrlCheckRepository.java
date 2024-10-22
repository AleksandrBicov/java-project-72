package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class UrlCheckRepository extends BaseRepository {

    public static void saveCheck(@NotNull UrlCheck checkUrl) throws SQLException {
        String sqlCheck = "INSERT INTO url_checks (urlId, statusCode, h1, title, description, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sqlCheck, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, checkUrl.getUrlId());
            preparedStatement.setInt(2, checkUrl.getStatusCode());
            preparedStatement.setString(3, checkUrl.getH1());
            preparedStatement.setString(4, checkUrl.getTitle());
            preparedStatement.setString(5, checkUrl.getDescription());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(checkUrl.getCreatedAt()));
            preparedStatement.executeUpdate();
        }
    }
}
