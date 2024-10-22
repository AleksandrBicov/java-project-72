package hexlet.code.Controller;

import hexlet.code.dto.url.BuildUrlPage;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
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

import java.net.URI;
import java.net.URL;


import java.sql.SQLException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

import static hexlet.code.repository.UrlCheckRepository.saveCheck;
import static io.javalin.rendering.template.TemplateUtil.model;


@Slf4j
public class Controller {
    public static void index(Context ctx) {
        BuildUrlPage page = new BuildUrlPage();
        ctx.render("index.jte",Collections.singletonMap("page", page));
    }

    public static void urlsPost(Context ctx) {
        String inputUrl = ctx.formParam("url");
        try {
            assert inputUrl != null;
            var uri = new URI(inputUrl.trim());
            URL url = uri.toURL();

            String name = createUrlString(url);
            LocalDateTime createdAt = LocalDateTime.now();

            Url nameUrl = new Url(name, createdAt);
            UrlsRepository.save(nameUrl);
            ctx.redirect("/urls");
        } catch (Exception e)  {
            log.error(String.valueOf(e));
            String name = ctx.formParam("name");
            LocalDateTime createdAt = LocalDateTime.now();
            var page = new BuildUrlPage(name, createdAt, e);
            ctx.render("index.jte", model("page", page));
        }
    }

    public static void urlsGet(Context ctx) throws SQLException {
        List<Url> urls = UrlsRepository.getEntities();
        var page = new UrlsPage(urls);
        ctx.render("urls/index.jte", Collections.singletonMap("page", page));
    }

    public static void show(Context ctx) throws SQLException {

        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlsRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));
        var page = new UrlPage(url);
        ctx.render("urls/show.jte", model("page", page));
    }

    public static void check(Context ctx) throws SQLException {

        var urlId = ctx.pathParamAsClass("id", Long.class).get();

        var url = UrlsRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Url not found"));

        String check = url.getName();

        try {
            HttpResponse<String> response = Unirest.get(check).asString();

            if (response.getStatus() == 200) {
                String html = response.getBody();
                Document doc = Jsoup.parse(html);

                int status = response.getStatus();
                String title = doc.title();
                String h1 = null;
                String description = null;
                LocalDateTime createdAt = LocalDateTime.now();

                Element h1Element = doc.selectFirst("h1");
                if (h1Element != null) {
                    h1 = h1Element.text();
                }

                Element metaDescriptionElement = doc.selectFirst("meta[name=description]");
                if (metaDescriptionElement != null) {
                    description = metaDescriptionElement.attr("content");
                }
            var checkUrl = new UrlCheck(urlId,status,title,h1,description,createdAt);
            saveCheck(checkUrl);
            }

        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    public static String createUrlString(URL url) {
        String protocol = url.getProtocol();
        String host = url.getHost();
        int port = url.getPort();
        String name = protocol + "://" + host;
        if (port != -1) {
            name += ":" + port;
        }
        return name;
    }
}
