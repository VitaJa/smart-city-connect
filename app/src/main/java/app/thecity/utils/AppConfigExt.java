package app.thecity.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

import app.thecity.AppConfig;
import app.thecity.data.ThisApplication;
import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfigExt {

    /* --------------- DONE EDIT CODE BELOW ------------------------------------------------------ */

    // define static variable for all config class
    public static AppConfig.General general = new AppConfig.General();
    public static AppConfig.Ads ads = new AppConfig.Ads();

    // Set data from remote config
    public static void setFromRemoteConfig(FirebaseRemoteConfig remote) {
        if (!remote.getString("web_url").isEmpty())
            AppConfig.general.web_url = remote.getString("web_url");

        if (!remote.getString("city_lat").isEmpty()) {
            try {
                AppConfig.general.city_lat = Double.parseDouble(remote.getString("city_lat"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("city_lng").isEmpty()) {
            try {
                AppConfig.general.city_lng = Double.parseDouble(remote.getString("city_lng"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("enable_news_info").isEmpty()) {
            AppConfig.general.enable_news_info = Boolean.parseBoolean(remote.getString("enable_news_info"));
        }

        if (!remote.getString("lazy_load").isEmpty()) {
            AppConfig.general.lazy_load = Boolean.parseBoolean(remote.getString("lazy_load"));
        }

        if (!remote.getString("enable_analytics").isEmpty()) {
            AppConfig.general.enable_analytics = Boolean.parseBoolean(remote.getString("enable_analytics"));
        }

        if (!remote.getString("refresh_img_notif").isEmpty()) {
            AppConfig.general.refresh_img_notif = Boolean.parseBoolean(remote.getString("refresh_img_notif"));
        }

        if (!remote.getString("sort_by_distance").isEmpty()) {
            AppConfig.general.sort_by_distance = Boolean.parseBoolean(remote.getString("sort_by_distance"));
        }

        if (!remote.getString("distance_metric_code").isEmpty()) {
            AppConfig.general.distance_metric_code = remote.getString("distance_metric_code");
        }

        if (!remote.getString("distance_metric_str").isEmpty()) {
            AppConfig.general.distance_metric_str = remote.getString("distance_metric_str");
        }

        if (!remote.getString("theme_color").isEmpty()) {
            AppConfig.general.theme_color = Boolean.parseBoolean(remote.getString("theme_color"));
        }

        if (!remote.getString("open_link_in_app").isEmpty()) {
            AppConfig.general.open_link_in_app = Boolean.parseBoolean(remote.getString("open_link_in_app"));
        }

        if (!remote.getString("limit_place_request").isEmpty()) {
            try {
                AppConfig.general.limit_place_request = Integer.parseInt(remote.getString("limit_place_request"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("limit_loadmore").isEmpty()) {
            try {
                AppConfig.general.limit_loadmore = Integer.parseInt(remote.getString("limit_loadmore"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("limit_news_request").isEmpty()) {
            try {
                AppConfig.general.limit_news_request = Integer.parseInt(remote.getString("limit_news_request"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("more_apps_url").isEmpty()) {
            AppConfig.general.more_apps_url = remote.getString("more_apps_url");
        }

        if (!remote.getString("contact_us_url").isEmpty()) {
            AppConfig.general.contact_us_url = remote.getString("contact_us_url");
        }


        /* ads configuration */

        if (!remote.getString("ad_enable").isEmpty()) {
            AppConfig.ads.ad_enable = Boolean.parseBoolean(remote.getString("ad_enable"));
        }

        if (!remote.getString("ad_networks").isEmpty()) {

            String[] arr = remote.getString("ad_networks").split(",");
            AdNetworkType[] adNetworkTypes = new AdNetworkType[arr.length];
            for (int i = 0; i < arr.length; i++) {
                try {
                    adNetworkTypes[i] = AdNetworkType.valueOf(arr[i].trim());
                } catch (Exception ignore) { }
            }
            AppConfig.ads.ad_networks = adNetworkTypes;
        }

        if (!remote.getString("ad_enable_gdpr").isEmpty()) {
            AppConfig.ads.ad_enable_gdpr = Boolean.parseBoolean(remote.getString("ad_enable_gdpr"));
        }

        if (!remote.getString("ad_main_banner").isEmpty()) {
            AppConfig.ads.ad_main_banner = Boolean.parseBoolean(remote.getString("ad_main_banner"));
        }

        if (!remote.getString("ad_main_interstitial").isEmpty()) {
            AppConfig.ads.ad_main_interstitial = Boolean.parseBoolean(remote.getString("ad_main_interstitial"));
        }

        if (!remote.getString("ad_place_details_banner").isEmpty()) {
            AppConfig.ads.ad_place_details_banner = Boolean.parseBoolean(remote.getString("ad_place_details_banner"));
        }

        if (!remote.getString("ad_news_details_banner").isEmpty()) {
            AppConfig.ads.ad_news_details_banner = Boolean.parseBoolean(remote.getString("ad_news_details_banner"));
        }

        if (!remote.getString("ad_inters_interval").isEmpty()) {
            try {
                AppConfig.ads.ad_inters_interval = Integer.parseInt(remote.getString("ad_inters_interval"));
            } catch (Exception ignored) {
            }
        }

        if (!remote.getString("ad_admob_publisher_id").isEmpty()) {
            AppConfig.ads.ad_admob_publisher_id = remote.getString("ad_admob_publisher_id");
        }
        if (!remote.getString("ad_admob_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_admob_banner_unit_id = remote.getString("ad_admob_banner_unit_id");
        }
        if (!remote.getString("ad_admob_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_admob_interstitial_unit_id = remote.getString("ad_admob_interstitial_unit_id");
        }
        if (!remote.getString("ad_admob_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_admob_rewarded_unit_id = remote.getString("ad_admob_rewarded_unit_id");
        }
        if (!remote.getString("ad_admob_open_app_unit_id").isEmpty()) {
            AppConfig.ads.ad_admob_open_app_unit_id = remote.getString("ad_admob_open_app_unit_id");
        }


        if (!remote.getString("ad_manager_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_manager_banner_unit_id = remote.getString("ad_manager_banner_unit_id");
        }
        if (!remote.getString("ad_manager_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_manager_interstitial_unit_id = remote.getString("ad_manager_interstitial_unit_id");
        }
        if (!remote.getString("ad_manager_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_manager_rewarded_unit_id = remote.getString("ad_manager_rewarded_unit_id");
        }
        if (!remote.getString("ad_manager_open_app_unit_id").isEmpty()) {
            AppConfig.ads.ad_manager_open_app_unit_id = remote.getString("ad_manager_open_app_unit_id");
        }

        if (!remote.getString("ad_fan_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_fan_banner_unit_id = remote.getString("ad_fan_banner_unit_id");
        }
        if (!remote.getString("ad_fan_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_fan_banner_unit_id = remote.getString("ad_fan_banner_unit_id");
        }
        if (!remote.getString("ad_fan_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_fan_rewarded_unit_id = remote.getString("ad_fan_rewarded_unit_id");
        }

        if (!remote.getString("ad_ironsource_app_key").isEmpty()) {
            AppConfig.ads.ad_ironsource_app_key = remote.getString("ad_ironsource_app_key");
        }
        if (!remote.getString("ad_ironsource_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_ironsource_banner_unit_id = remote.getString("ad_ironsource_banner_unit_id");
        }
        if (!remote.getString("ad_ironsource_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_ironsource_rewarded_unit_id = remote.getString("ad_ironsource_rewarded_unit_id");
        }
        if (!remote.getString("ad_ironsource_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_ironsource_interstitial_unit_id = remote.getString("ad_ironsource_interstitial_unit_id");
        }

        if (!remote.getString("ad_unity_game_id").isEmpty()) {
            AppConfig.ads.ad_unity_game_id = remote.getString("ad_unity_game_id");
        }
        if (!remote.getString("ad_unity_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_unity_banner_unit_id = remote.getString("ad_unity_banner_unit_id");
        }
        if (!remote.getString("ad_unity_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_unity_rewarded_unit_id = remote.getString("ad_unity_rewarded_unit_id");
        }
        if (!remote.getString("ad_unity_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_unity_interstitial_unit_id = remote.getString("ad_unity_interstitial_unit_id");
        }

        if (!remote.getString("ad_applovin_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_applovin_banner_unit_id = remote.getString("ad_applovin_banner_unit_id");
        }
        if (!remote.getString("ad_applovin_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_applovin_interstitial_unit_id = remote.getString("ad_applovin_interstitial_unit_id");
        }
        if (!remote.getString("ad_applovin_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_applovin_rewarded_unit_id = remote.getString("ad_applovin_rewarded_unit_id");
        }
        if (!remote.getString("ad_applovin_open_app_unit_id").isEmpty()) {
            AppConfig.ads.ad_applovin_open_app_unit_id = remote.getString("ad_applovin_open_app_unit_id");
        }

        if (!remote.getString("ad_applovin_banner_zone_id").isEmpty()) {
            AppConfig.ads.ad_applovin_banner_zone_id = remote.getString("ad_applovin_banner_zone_id");
        }
        if (!remote.getString("ad_applovin_interstitial_zone_id").isEmpty()) {
            AppConfig.ads.ad_applovin_interstitial_zone_id = remote.getString("ad_applovin_interstitial_zone_id");
        }
        if (!remote.getString("ad_applovin_rewarded_zone_id").isEmpty()) {
            AppConfig.ads.ad_applovin_rewarded_zone_id = remote.getString("ad_applovin_rewarded_zone_id");
        }
        if (!remote.getString("ad_applovin_rewarded_zone_id").isEmpty()) {
            AppConfig.ads.ad_applovin_rewarded_zone_id = remote.getString("ad_applovin_rewarded_zone_id");
        }

        if (!remote.getString("ad_startapp_app_id").isEmpty()) {
            AppConfig.ads.ad_startapp_app_id = remote.getString("ad_startapp_app_id");
        }

        if (!remote.getString("ad_wortise_app_id").isEmpty()) {
            AppConfig.ads.ad_wortise_app_id = remote.getString("ad_wortise_app_id");
        }
        if (!remote.getString("ad_wortise_banner_unit_id").isEmpty()) {
            AppConfig.ads.ad_wortise_banner_unit_id = remote.getString("ad_wortise_banner_unit_id");
        }
        if (!remote.getString("ad_wortise_interstitial_unit_id").isEmpty()) {
            AppConfig.ads.ad_wortise_interstitial_unit_id = remote.getString("ad_wortise_interstitial_unit_id");
        }
        if (!remote.getString("ad_wortise_rewarded_unit_id").isEmpty()) {
            AppConfig.ads.ad_wortise_rewarded_unit_id = remote.getString("ad_wortise_rewarded_unit_id");
        }
        if (!remote.getString("ad_wortise_open_app_unit_id").isEmpty()) {
            AppConfig.ads.ad_wortise_open_app_unit_id = remote.getString("ad_wortise_open_app_unit_id");
        }

        saveToSharedPreference();
    }

    // Set data from shared preference
    public static void setFromSharedPreference() {
        Context context = ThisApplication.getInstance();
        SharedPreferences pref = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        String jsonGeneral = pref.getString("APP_CONFIG_GENERAL", null);
        String jsonAds = pref.getString("APP_CONFIG_ADS", null);

        if (!TextUtils.isEmpty(jsonGeneral)) {
            AppConfig.general = new Gson().fromJson(jsonGeneral, AppConfig.General.class);
        }

        if (!TextUtils.isEmpty(jsonAds)) {
            AppConfig.ads = new Gson().fromJson(jsonAds, AppConfig.Ads.class);
        }
    }

    // Save data from shared preference
    private static void saveToSharedPreference() {
        Context context = ThisApplication.getInstance();
        SharedPreferences pref = context.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        pref.edit().putString("APP_CONFIG_GENERAL", new Gson().toJson(AppConfig.general)).apply();
        pref.edit().putString("APP_CONFIG_ADS", new Gson().toJson(AppConfig.ads)).apply();
    }
}
