package hexlet.code.Controller;

import hexlet.code.dto.url.BuildUrlPage;
import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Context;



import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

import static io.javalin.rendering.template.TemplateUtil.model;


public class Controller {
    public static void index(Context ctx) {
        ctx.render("index.jte");
    }

    public static void urlsPost(Context ctx) {
        try {
            URI uri = ctx.formParamAsClass("name", URI.class).get();
            URL url = uri.toURL();

            String protocol = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            // Формирование строки для добавления в БД
            String name = protocol + "://" + host;
            if (port != -1) {
                name += ":" + port;
            }

            LocalDateTime createdAt = LocalDateTime.now();
            Url nameUrl = new Url(name, createdAt);
            UrlsRepository.save(nameUrl);
            ctx.redirect("/urls");
        } catch (Exception e) {
            String name = ctx.formParam("name");
            LocalDateTime createdAt = LocalDateTime.now();
            var page = new BuildUrlPage(name, createdAt, e);
            ctx.render("error.jte", model("page", page));
        }
    }
}
