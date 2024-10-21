package hexlet.code.Controller;

import hexlet.code.dto.url.BuildUrlPage;
import hexlet.code.dto.url.UrlPage;
import hexlet.code.dto.url.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;


import java.net.URI;
import java.net.URL;


import java.sql.SQLException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

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
