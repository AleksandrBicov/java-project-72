package hexlet.code.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;

public class TemplateEngineConfig {

    // Метод для создания инстанса движка шаблонизатора JTE

    public static TemplateEngine createTemplateEngine() {
        // Получаем ClassLoader
        ClassLoader classLoader = TemplateEngineConfig.class.getClassLoader();

        // Создаем ResourceCodeResolver и указываем путь к шаблонам
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);

        // Создаем и настраиваем TemplateEngine
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);

        return templateEngine;
    }
}