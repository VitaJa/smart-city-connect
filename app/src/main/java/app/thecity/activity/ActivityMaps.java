package app.thecity.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.thecity.AppConfig;
import app.thecity.R;
import app.thecity.data.Constant;
import app.thecity.data.DatabaseHandler;
import app.thecity.model.Category;
import app.thecity.model.Place;
import app.thecity.utils.PermissionUtil;
import app.thecity.utils.Tools;

public class ActivityMaps extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_OBJ = "key.EXTRA_OBJ";

    private GoogleMap mMap;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private DatabaseHandler db;
    private ClusterManager<Place> mClusterManager;
    private View parent_view;
    private int cat[];
    private PlaceMarkerRenderer placeMarkerRenderer;

    // for single place
    private Place ext_place = null;
    private boolean isSinglePlace;
    HashMap<String, Place> hashMapPlaces = new HashMap<>();
    private HashMap<String, Marker> markerPlaces = new HashMap<>();
    private HashMap<String, Integer> placesPosition = new HashMap<>();
    private List<Place> items = new ArrayList<>();

    // id category
    private int cat_id = -1;

    private Category cur_category;

    // view for custom marker
    private ImageView icon, marker_bg;
    private View marker_view;

    private boolean showSlider = true;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        parent_view = findViewById(android.R.id.content);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        marker_view = inflater.inflate(R.layout.maps_marker, null);
        icon = (ImageView) marker_view.findViewById(R.id.marker_icon);
        marker_bg = (ImageView) marker_view.findViewById(R.id.marker_bg);
        viewPager = findViewById(R.id.view_pager);

        ext_place = (Place) getIntent().getSerializableExtra(EXTRA_OBJ);
        isSinglePlace = (ext_place != null);

        db = new DatabaseHandler(this);
        initMapFragment();
        initToolbar();

        cat = getResources().getIntArray(R.array.id_category);

        // for system bar in lollipop
        Tools.systemBarLolipop(this);
        Tools.RTLMode(getWindow());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = Tools.configActivityMaps(googleMap);
        CameraUpdate location;
        if (isSinglePlace) {
            marker_bg.setColorFilter(getResources().getColor(R.color.marker_secondary));
            MarkerOptions markerOptions = new MarkerOptions().title(ext_place.name).position(ext_place.getPosition());
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(ActivityMaps.this, marker_view)));
            mMap.addMarker(markerOptions);
            location = CameraUpdateFactory.newLatLngZoom(ext_place.getPosition(), 12);
            actionBar.setTitle(ext_place.name);

            loadClusterManager(new ArrayList<>());
        } else {
            location = CameraUpdateFactory.newLatLngZoom(new LatLng(AppConfig.general.city_lat, AppConfig.general.city_lng), 9);

            mClusterManager = new ClusterManager<>(this, this.mMap);
            placeMarkerRenderer = new PlaceMarkerRenderer(this, this.mMap, mClusterManager);
            initClusterWithSlider();
            mClusterManager.setRenderer(placeMarkerRenderer);
            this.mMap.setOnCameraIdleListener(mClusterManager);

            loadClusterManager(db.getAllPlace());
        }
        mMap.animateCamera(location);
        mMap.setOnInfoWindowClickListener(marker -> {
            Place place;
            if (hashMapPlaces.get(marker.getId()) != null) {
                place = (Place) hashMapPlaces.get(marker.getId());
            } else {
                place = ext_place;
            }
            ActivityPlaceDetail.navigate(ActivityMaps.this, parent_view, place);
        });

        mMap.setOnMapClickListener(latLng -> toggleViewPager(!showSlider));

        showMyLocation();
        initViewPager();
    }

    private void initClusterWithSlider() {
        mClusterManager.setOnClusterItemClickListener(item -> {
            Integer position = placesPosition.get(item.place_id + "");
            if (position != null) {
                viewPager.setCurrentItem(position, true);
            }
            return false;
        });
        mClusterManager.setOnClusterItemInfoWindowClickListener(item -> ActivityPlaceDetail.navigate(ActivityMaps.this, parent_view, item));
    }

    @SuppressLint("MissingPermission")
    private void showMyLocation() {
        if (PermissionUtil.isLocationGranted(this)) {
            // Enable / Disable my location button
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    try {
                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            showAlertDialogGps();
                        } else {
                            Location loc = Tools.getLastKnownLocation(ActivityMaps.this);
                            CameraUpdate myCam = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 12);
                            mMap.animateCamera(myCam);
                        }
                    } catch (Exception e) {
                    }
                    return true;
                }
            });
        }
    }

    private void loadClusterManager(List<Place> items) {
        this.items = new ArrayList<>();
        placesPosition.clear();
        if (ext_place != null) {
            placesPosition.put(ext_place.place_id + "", 0);
            this.items.add(ext_place);
        }
        int index = 0, last_size = placesPosition.size();
        for (Place p : items) {
            this.items.add(p);
            placesPosition.put(p.place_id + "", last_size + index);
            index++;
        }
        if(mClusterManager != null) {
            mClusterManager.clearItems();
            mClusterManager.cluster();
            mClusterManager.addItems(this.items);
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.activity_title_maps);
        Tools.setActionBarColor(this, actionBar);
    }

    private void initMapFragment() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private class PlaceMarkerRenderer extends DefaultClusterRenderer<Place> {
        public PlaceMarkerRenderer(Context context, GoogleMap map, ClusterManager<Place> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(Place item, MarkerOptions markerOptions) {
            if (cat_id == -1) { // all place
                icon.setImageResource(R.drawable.round_shape);
            } else {
                icon.setImageResource(cur_category.icon);
            }
            marker_bg.setColorFilter(getResources().getColor(R.color.marker_primary));
            markerOptions.title(item.name);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Tools.createBitmapFromView(ActivityMaps.this, marker_view)));
            if (ext_place != null && ext_place.place_id == item.place_id) {
                markerOptions.visible(false);
            }
        }

        @Override
        protected void onClusterItemRendered(Place item, Marker marker) {
            hashMapPlaces.put(marker.getId(), item);
            markerPlaces.put(item.place_id + "", marker);
            super.onClusterItemRendered(item, marker);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        } else {
            String category_text;
            if (item.getItemId() != R.id.menu_category) {
                category_text = item.getTitle().toString();
                int itemId = item.getItemId();
                if (itemId == R.id.nav_all) {
                    cat_id = -1;
                } else if (itemId == R.id.nav_featured) {
                    cat_id = cat[10];
                } else if (itemId == R.id.nav_tour) {
                    cat_id = cat[0];
                } else if (itemId == R.id.nav_food) {
                    cat_id = cat[1];
                } else if (itemId == R.id.nav_hotels) {
                    cat_id = cat[2];
                } else if (itemId == R.id.nav_ent) {
                    cat_id = cat[3];
                } else if (itemId == R.id.nav_sport) {
                    cat_id = cat[4];
                } else if (itemId == R.id.nav_shop) {
                    cat_id = cat[5];
                } else if (itemId == R.id.nav_transport) {
                    cat_id = cat[6];
                } else if (itemId == R.id.nav_religion) {
                    cat_id = cat[7];
                } else if (itemId == R.id.nav_public) {
                    cat_id = cat[8];
                } else if (itemId == R.id.nav_money) {
                    cat_id = cat[9];
                }

                clearViewPager();

                // get category object when menu click
                cur_category = db.getCategory(cat_id);

                if (isSinglePlace) {
                    isSinglePlace = false;
                    mClusterManager = new ClusterManager<>(this, mMap);
                    mMap.setOnCameraIdleListener(mClusterManager);
                }

                List<Place> places = db.getAllPlaceByCategory(cat_id);
                loadClusterManager(places);
                if (places.size() == 0) {
                    Snackbar.make(parent_view, getString(R.string.no_item_at) + " " + item.getTitle().toString(), Snackbar.LENGTH_LONG).show();
                }
                placeMarkerRenderer = new PlaceMarkerRenderer(this, mMap, mClusterManager);
                initClusterWithSlider();
                mClusterManager.setRenderer(placeMarkerRenderer);

                actionBar.setTitle(category_text);

                showSlider = places.size() != 0;
                toggleViewPager(showSlider);
                if (places.size() != 0) {
                    initViewPager();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialogGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_content_gps);
        builder.setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void initViewPager() {
        mMap.setPadding(0, 0, 0, Tools.dpToPx(this, 140));
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(Tools.dpToPx(this, 20), 0, Tools.dpToPx(this, 20), 0);
        viewPager.setPageMargin(Tools.dpToPx(this, -6));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMap.stopAnimation();
                CameraUpdate location = CameraUpdateFactory.newLatLngZoom(items.get(position).getPosition(), 16);
                mMap.animateCamera(location, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFinish() {
                        Marker marker = markerPlaces.get(items.get(position).place_id + "");
                        if (marker != null && !marker.isInfoWindowShown()) {
                            marker.showInfoWindow();
                        } else {
                            new Handler().postDelayed(() -> {
                                Marker marker_ = markerPlaces.get(items.get(position).place_id + "");
                                if (marker_ != null && !marker_.isInfoWindowShown()) {
                                    marker_.showInfoWindow();
                                }
                            }, 1000);
                        }
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void clearViewPager() {
        mMap.setPadding(0, 0, 0, 0);
        viewPager.setAdapter(null);
    }

    private void toggleViewPager(boolean show) {
        showSlider = show;
        float heightMax = viewPager.getHeight();
        float start = show ? heightMax : 0, end = show ? 0 : heightMax;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewPager, "translationY", start, end);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mMap.setPadding(0, 0, 0, show ? Tools.dpToPx(ActivityMaps.this, 140) : 0);
            }
        });
        objectAnimator.start();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Place place = items.get(position);
            View view = layoutInflater.inflate(R.layout.item_place_slider, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(place.name);
            ImageView image = view.findViewById(R.id.image);
            View lyt_address = view.findViewById(R.id.lyt_address);
            View lyt_distance = view.findViewById(R.id.lyt_distance);
            TextView address = view.findViewById(R.id.address);
            Tools.displayImageThumb(ActivityMaps.this, image, Constant.getURLimgPlace(place.image), 0.5f);
            address.setText(place.address);

            if (place.distance == -1) {
                lyt_distance.setVisibility(View.GONE);
            } else {
                lyt_distance.setVisibility(View.VISIBLE);
                ((TextView) view.findViewById(R.id.distance)).setText(Tools.getFormatedDistance(place.distance));
            }

            view.findViewById(R.id.lyt_parent).setOnClickListener(v ->
                    ActivityPlaceDetail.navigate(ActivityMaps.this, image, place)
            );
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}
