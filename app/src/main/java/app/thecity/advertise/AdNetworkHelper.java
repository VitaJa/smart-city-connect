package app.thecity.advertise;

import android.app.Activity;
import android.content.Context;

import app.thecity.AppConfig;
import app.thecity.R;
import dreamspace.ads.sdk.AdConfig;
import dreamspace.ads.sdk.AdNetwork;
import app.thecity.BuildConfig;
import dreamspace.ads.sdk.gdpr.GDPR;
import dreamspace.ads.sdk.gdpr.LegacyGDPR;
import dreamspace.ads.sdk.gdpr.UMP;
import dreamspace.ads.sdk.listener.AdBannerListener;
import dreamspace.ads.sdk.listener.AdOpenListener;
import dreamspace.ads.sdk.listener.AdRewardedListener;

public class AdNetworkHelper {

    private Activity activity;
    private AdNetwork adNetwork;
    private LegacyGDPR legacyGDPR;
    private GDPR gdpr;

    public AdNetworkHelper(Activity activity) {
        this.activity = activity;
        adNetwork = new AdNetwork(activity);
        legacyGDPR = new LegacyGDPR(activity);
        gdpr = new GDPR(activity);
    }

    public void updateConsentStatus() {
        if (!AppConfig.ads.ad_enable || !AppConfig.ads.ad_enable_gdpr) return;
        gdpr.updateGDPRConsentStatus();
    }

    public static void initConfig() {
        AdConfig.ad_enable = AppConfig.ads.ad_enable;
        AdConfig.ad_networks = AppConfig.ads.ad_networks;
        AdConfig.retry_from_start_max = 5;
        AdConfig.enable_gdpr = AppConfig.ads.ad_enable_gdpr;

        AdConfig.ad_replace_unsupported_open_app_with_interstitial_on_splash = false;
        AdConfig.ad_inters_interval = AppConfig.ads.ad_inters_interval;
        AdConfig.ad_enable_open_app = false;
        AdConfig.limit_time_open_app_loading = 5;
        AdConfig.debug_mode = BuildConfig.DEBUG;

        AdConfig.ad_admob_publisher_id = AppConfig.ads.ad_admob_publisher_id;
        AdConfig.ad_admob_banner_unit_id = AppConfig.ads.ad_admob_banner_unit_id;
        AdConfig.ad_admob_interstitial_unit_id = AppConfig.ads.ad_admob_interstitial_unit_id;
        AdConfig.ad_admob_rewarded_unit_id = AppConfig.ads.ad_admob_rewarded_unit_id;
        AdConfig.ad_admob_open_app_unit_id = AppConfig.ads.ad_admob_open_app_unit_id;

        AdConfig.ad_manager_banner_unit_id = AppConfig.ads.ad_manager_banner_unit_id;
        AdConfig.ad_manager_interstitial_unit_id = AppConfig.ads.ad_manager_interstitial_unit_id;
        AdConfig.ad_manager_rewarded_unit_id = AppConfig.ads.ad_admob_rewarded_unit_id;
        AdConfig.ad_manager_open_app_unit_id = AppConfig.ads.ad_manager_open_app_unit_id;

        AdConfig.ad_fan_banner_unit_id = AppConfig.ads.ad_fan_banner_unit_id;
        AdConfig.ad_fan_interstitial_unit_id = AppConfig.ads.ad_fan_interstitial_unit_id;
        AdConfig.ad_fan_rewarded_unit_id = AppConfig.ads.ad_fan_rewarded_unit_id;

        AdConfig.ad_ironsource_app_key = AppConfig.ads.ad_ironsource_app_key;
        AdConfig.ad_ironsource_banner_unit_id = AppConfig.ads.ad_ironsource_banner_unit_id;
        AdConfig.ad_ironsource_rewarded_unit_id = AppConfig.ads.ad_ironsource_rewarded_unit_id;
        AdConfig.ad_ironsource_interstitial_unit_id = AppConfig.ads.ad_ironsource_interstitial_unit_id;

        AdConfig.ad_unity_game_id = AppConfig.ads.ad_unity_game_id;
        AdConfig.ad_unity_banner_unit_id = AppConfig.ads.ad_unity_banner_unit_id;
        AdConfig.ad_unity_rewarded_unit_id = AppConfig.ads.ad_unity_rewarded_unit_id;
        AdConfig.ad_unity_interstitial_unit_id = AppConfig.ads.ad_unity_interstitial_unit_id;

        AdConfig.ad_applovin_banner_unit_id = AppConfig.ads.ad_applovin_banner_unit_id;
        AdConfig.ad_applovin_interstitial_unit_id = AppConfig.ads.ad_applovin_interstitial_unit_id;
        AdConfig.ad_applovin_rewarded_unit_id = AppConfig.ads.ad_applovin_rewarded_unit_id;
        AdConfig.ad_applovin_open_app_unit_id = AppConfig.ads.ad_applovin_open_app_unit_id;

        AdConfig.ad_applovin_banner_zone_id = AppConfig.ads.ad_applovin_banner_zone_id;
        AdConfig.ad_applovin_interstitial_zone_id = AppConfig.ads.ad_applovin_interstitial_zone_id;
        AdConfig.ad_applovin_rewarded_zone_id = AppConfig.ads.ad_applovin_rewarded_zone_id;

        AdConfig.ad_startapp_app_id = AppConfig.ads.ad_startapp_app_id;

        AdConfig.ad_wortise_app_id = AppConfig.ads.ad_wortise_app_id;
        AdConfig.ad_wortise_banner_unit_id = AppConfig.ads.ad_wortise_banner_unit_id;
        AdConfig.ad_wortise_interstitial_unit_id = AppConfig.ads.ad_wortise_interstitial_unit_id;
        AdConfig.ad_wortise_rewarded_unit_id = AppConfig.ads.ad_wortise_rewarded_unit_id;
        AdConfig.ad_wortise_open_app_unit_id = AppConfig.ads.ad_wortise_open_app_unit_id;

    }

    public void init() {
        AdNetworkHelper.initConfig();
        adNetwork.init();
    }

    public void loadBannerAd(boolean enable) {
        adNetwork.loadBannerAd(enable, activity.findViewById(R.id.ad_container));
    }

    public void loadInterstitialAd(boolean enable) {
        adNetwork.loadInterstitialAd(enable);
    }

    public boolean showInterstitialAd(boolean enable) {
        return adNetwork.showInterstitialAd(enable);
    }

    public void loadRewardedAd(boolean enable, AdRewardedListener listener) {
        adNetwork.loadRewardedAd(enable, listener);
    }

    public boolean showRewardedAd(boolean enable, AdRewardedListener listener) {
        return adNetwork.showRewardedAd(enable, listener);
    }

    public void loadAndShowOpenAppAd(Activity activity, boolean enable, AdOpenListener listener) {
        adNetwork.loadAndShowOpenAppAd(activity, enable, listener);
    }

    public void destroyAndDetachBanner() {
        adNetwork.destroyAndDetachBanner();
    }

    public void loadShowUMPConsentForm() {
        new UMP(activity).loadShowConsentForm();
    }

}
