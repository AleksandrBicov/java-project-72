package hexlet.code.dto.url;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UrlPage extends BasePage {
    private Url url;
    private List<UrlCheck> urlCheck;
}
