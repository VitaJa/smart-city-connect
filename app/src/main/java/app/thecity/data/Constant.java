package app.thecity.data;
import app.thecity.AppConfig;

public class Constant {

    // image file url
    public static String getURLimgPlace(String file_name) {
        return AppConfig.general.web_url + "uploads/place/" + file_name;
    }
    public static String getURLimgNews(String file_name) {
        return AppConfig.general.web_url + "uploads/news/" + file_name;
    }

    // for search logs Tag
    public static final String LOG_TAG = "CITY_LOG";

    // Google analytics event category
    public enum Event {
        FAVORITES,
        THEME,
        NOTIFICATION,
        REFRESH
    }

}
