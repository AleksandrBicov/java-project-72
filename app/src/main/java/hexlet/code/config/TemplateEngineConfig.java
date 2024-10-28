package hexlet.code.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

public class TemplateEngineConfig {

    // Метод для создания инстанса движка шаблонизатора JTE
    public static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = TemplateEngineConfig.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        return templateEngine;
    }
}
