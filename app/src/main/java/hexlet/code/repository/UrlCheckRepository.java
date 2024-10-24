package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static List<UrlCheck> find(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE urlId = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            List<UrlCheck> checks = new ArrayList<>();
            while (resultSet.next()) {
                long checkId = resultSet.getLong("id");
                int statusCode = resultSet.getInt("statusCode");
                String h1 = resultSet.getString("h1");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                var urlCheck = new UrlCheck(urlId,statusCode,title,h1,description,createdAt);
                urlCheck.setId(checkId);
                checks.add(urlCheck);
            }
            return checks;
        }
    }
}
