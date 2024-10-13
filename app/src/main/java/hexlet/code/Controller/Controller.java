package hexlet.code.Controller;

import io.javalin.http.Context;

public class Controller {
    public static void index(Context ctx) {
        ctx.render("index.jte");
    }
}
