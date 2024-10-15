package hexlet.code.dto.url;

import hexlet.code.model.Url;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UrlsPage {
    private List<Url> urls;
}