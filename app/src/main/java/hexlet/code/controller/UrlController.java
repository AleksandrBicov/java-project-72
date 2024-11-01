package hexlet.code.controller;

import hexlet.code.dto.url.BuildUrlPage;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlsRepository;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;

import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;


import java.sql.SQLException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.javalin.rendering.template.TemplateUtil.model;


@Slf4j
public class UrlController {
    public static void index(Context ctx) {
        BuildUrlPage page = new BuildUrlPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    }

    public static void addUrl(Context ctx) throws SQLException, MalformedURLException {
        String inputUrl = Objects.requireNonNull(ctx.formParam("url")).trim();

        URI parsedUrl;
        try {
            parsedUrl = new URI(inputUrl);
        } catch (Exception e)  {
            log.error(String.valueOf(e));
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.redirect("/");
            return;
        }

        String name = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getScheme(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();

        if (UrlsRepository.find(name).orElse(false)) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.redirect("/urls");
            return;
        }

        Url nameUrl = new Url(name);
        UrlsRepository.save(nameUrl);
        ctx.sessionAttribute("flash", "URL успешно добавлен!");
        ctx.redirect("/urls");
    }

    public static void urlList(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlCheckRepository.findLatestChecks();
        var page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        List<UrlCheck> checks = UrlCheckRepository.find(id);
        var page = new UrlPage(url, checks);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException {

        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        String check = url.getName();

        try {
            HttpResponse<String> response = Unirest.get(check).asString();

            String html = response.getBody();
            Document doc = Jsoup.parse(html);

            int status = response.getStatus();
            String title = doc.title();
            String h1 = null;
            String description = null;

            Element h1Element = doc.selectFirst("h1");
            if (h1Element != null) {
                h1 = h1Element.text();
            }

            Element metaDescriptionElement = doc.selectFirst("meta[name=description]");
            if (metaDescriptionElement != null) {
                description = metaDescriptionElement.attr("content");
            }
            var checkUrl = new UrlCheck(urlId, status, title, h1, description);
            UrlCheckRepository.saveCheck(checkUrl);
            ctx.sessionAttribute("flash", "Страница успешно проверена");

        } catch (UnirestException e) {
            log.error(String.valueOf(e));
        }
        show(ctx);
    }
}
