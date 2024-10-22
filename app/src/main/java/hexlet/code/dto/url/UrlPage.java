package hexlet.code.dto.url;

import hexlet.code.model.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.format.DateTimeFormatter;


@AllArgsConstructor
@Getter
public class UrlPage {
    private Url url;

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return url.getCreatedAt().format(formatter);
    }
}
