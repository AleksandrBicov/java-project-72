package hexlet.code.utils;

public class NamedRoutes {
    public static String rootPath() {
        return "/";
    }

    public static String urlsPost() {
        return "/urls";
    }

    public static String urlsGet() {
        return "/urls";
    }

    public static String urlsPath(String id) {
        return "/urls/" + id;
    }

    public static String check(String id) {
        return "urls/" + id + "/checks";
    }
}
