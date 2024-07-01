package app.thecity;

import java.io.Serializable;

import app.thecity.utils.AppConfigExt;
import dreamspace.ads.sdk.data.AdNetworkType;

public class AppConfig extends AppConfigExt implements Serializable {

    /* -------------------------------------- INSTRUCTION : ----------------------------------------
     * This is config file used for this app, you can configure Ads, Notification, and General data from this file
     * some values are not explained and can be understood easily according to the variable name
     * value can change remotely (optional), please read documentation to follow instruction
     *
     * variable with UPPERCASE name will NOT fetch / replace with remote config
     * variable with LOWERCASE name will fetch / replace with remote config
     * See video Remote Config tutorial https://www.youtube.com/watch?v=tOKXwOTqOzA
     ----------------------------------------------------------------------------------------------*/

    /* set true for fetch config with firebase remote config, */
    public static final boolean USE_REMOTE_CONFIG = true;

    /* force rtl layout direction */
    public static final boolean RTL_LAYOUT = false;

    /* config for General Application */
    public static class General implements Serializable {

        /* Edit WEB_URL with your url. Make sure you have backslash('/') in the end url */
        public String web_url = "https://city-connect.app/";

        /* for map default zoom */
        public double city_lat = 0.3130293;
        public double city_lng = 32.5991254;

        /* this flag if you want to hide menu news info */
        public boolean enable_news_info = true;

        /* if you place data more than 200 items please set TRUE */
        public boolean lazy_load = false;

        /* flag for tracking analytics */
        public boolean enable_analytics = true;

        /* clear image cache when receive push notifications */
        public boolean refresh_img_notif = true;

        /* when user enable gps, places will sort by distance */
        public boolean sort_by_distance = true;

        /* distance metric, fill with KILOMETER or MILE only */
        public String distance_metric_code = "KILOMETER";

        /* related to UI display string */
        public String distance_metric_str = "Km";

        /* flag for enable disable theme color chooser, in Setting */
        public boolean theme_color = true;

        /* true for open link in internal app browser, not external app browser */
        public boolean open_link_in_app = true;

        /* this limit value used for give pagination (request and display) to decrease payload */
        public int limit_place_request = 40;
        public int limit_loadmore = 40;
        public int limit_news_request = 40;

        /* 2 links below will use on setting page */
        public String more_apps_url = "https://city-connect.app/";
        public String contact_us_url = "https://city-connect.app/";
    }

    /* config for Ad Network */
    public static class Ads implements Serializable {

        /* enable disable ads */
        public boolean ad_enable = false;

        /* MULTI Ad network selection,
         * Fill this array to enable ad backup flow, left this empty to use single ad_network above
         * app will try show sequentially from this array
         * example flow ADMOB > FAN > IRONSOURCE
         *
         * OPTION :
         * ADMOB,MANAGER, FAN, UNITY, IRONSOURCE, APPLOVIN, APPLOVIN_MAX, APPLOVIN_DISCOVERY,
         * STARTAPP, WORTISE, FAN_BIDDING_ADMOB, FAN_BIDDING_AD_MANAGER, FAN_BIDDING_APPLOVIN_MAX,
         * FAN_BIDDING_IRONSOURCE
         * */
        public AdNetworkType[] ad_networks = {
                AdNetworkType.ADMOB,
                AdNetworkType.FAN,
                AdNetworkType.IRONSOURCE,
        };

        public boolean ad_enable_gdpr = true;

        /* disable enable ads each page */
        public boolean ad_main_banner = true;
        public boolean ad_main_interstitial = true;
        public boolean ad_place_details_banner = true;
        public boolean ad_news_details_banner = true;

        /* show interstitial after several action, this value for action counter */
        public int ad_inters_interval = 5;

        /* ad unit for ADMOB */
        public String ad_admob_publisher_id = "pub-3940256099942544";
        public String ad_admob_banner_unit_id = "ca-app-pub-3940256099942544/6300978111";
        public String ad_admob_interstitial_unit_id = "ca-app-pub-3940256099942544/1033173712";
        public String ad_admob_rewarded_unit_id = "ca-app-pub-3940256099942544/5224354917";
        public String ad_admob_open_app_unit_id = "ca-app-pub-3940256099942544/9257395921";

        /* ad unit for Google Ad Manager */
        public String ad_manager_banner_unit_id = "/6499/example/banner";
        public String ad_manager_interstitial_unit_id = "/6499/example/interstitial";
        public String ad_manager_rewarded_unit_id = "/6499/example/rewarded";
        public String ad_manager_open_app_unit_id = "/6499/example/app-open";

        /* ad unit for FAN */
        public String ad_fan_banner_unit_id = "YOUR_PLACEMENT_ID";
        public String ad_fan_interstitial_unit_id = "YOUR_PLACEMENT_ID";
        public String ad_fan_rewarded_unit_id = "YOUR_PLACEMENT_ID";

        /* ad unit for IRON SOURCE */
        public String ad_ironsource_app_key = "170112cfd";
        public String ad_ironsource_banner_unit_id = "DefaultBanner";
        public String ad_ironsource_rewarded_unit_id = "DefaultRewardedVideo";
        public String ad_ironsource_interstitial_unit_id = "DefaultInterstitial";

        /* ad unit for UNITY */
        public String ad_unity_game_id = "4988568";
        public String ad_unity_banner_unit_id = "Banner_Android";
        public String ad_unity_rewarded_unit_id = "Rewarded_Android";
        public String ad_unity_interstitial_unit_id = "Interstitial_Android";

        /* ad unit for APPLOVIN MAX */
        public String ad_applovin_banner_unit_id = "a3a3a5b44c763304";
        public String ad_applovin_interstitial_unit_id = "35f9c01af124fcb9";
        public String ad_applovin_rewarded_unit_id = "21dba76a66f7c9fe";
        public String ad_applovin_open_app_unit_id = "7c3fcecd43d3f90c";

        /* ad unit for APPLOVIN DISCOVERY */
        public String ad_applovin_banner_zone_id = "df40a31072feccab";
        public String ad_applovin_interstitial_zone_id = "d0eea040d4bd561e";
        public String ad_applovin_rewarded_zone_id = "5d799aeefef733a1";

        /* ad unit for STARTAPP */
        public String ad_startapp_app_id = "0";

        /* ad unit for WORTISE */
        public String ad_wortise_app_id = "test-app-id";
        public String ad_wortise_banner_unit_id = "test-banner";
        public String ad_wortise_interstitial_unit_id = "test-interstitial";
        public String ad_wortise_rewarded_unit_id = "test-rewarded";
        public String ad_wortise_open_app_unit_id = "test-app-open";
    }

}
