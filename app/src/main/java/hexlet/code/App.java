package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

import io.javalin.rendering.template.JavalinJte;

public class App {

    public static Javalin getApp() {

        // BEGIN
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;

        // END

    }


    public static void main(String[] args) {
        Javalin app = getApp();
        app.start(7070);
    }
}