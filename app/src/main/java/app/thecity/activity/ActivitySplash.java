package app.thecity.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import app.thecity.AppConfig;
import app.thecity.R;
import app.thecity.data.SharedPref;
import app.thecity.data.ThisApplication;
import app.thecity.utils.PermissionUtil;
import app.thecity.utils.Tools;

public class ActivitySplash extends AppCompatActivity {

    private SharedPref sharedPref;
    private View parent_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        parent_view = findViewById(R.id.parent_view);

        sharedPref = new SharedPref(this);
        parent_view.setBackgroundColor(sharedPref.getThemeColorInt());

        // permission checker for android M or higher
        if (Tools.needRequestPermission()) {
            String[] permission = PermissionUtil.getDeniedPermission(this);
            if (permission.length != 0) {
                requestPermissions(permission, 200);
            } else {
                requestRemoteConfig();
            }
        } else {
            requestRemoteConfig();
        }

        // for system bar in lollipop
        Tools.systemBarLolipop(this);

        Tools.RTLMode(getWindow());
    }

    private void requestRemoteConfig() {
        boolean connectToInternet = Tools.cekConnection(this);
        if (!AppConfig.USE_REMOTE_CONFIG || !connectToInternet) {
            AppConfig.setFromSharedPreference();
            startActivityMainDelay(false);
            return;
        }
        Log.d("REMOTE_CONFIG", "requestRemoteConfig");
        FirebaseRemoteConfig firebaseRemoteConfig = ThisApplication.getInstance().getFirebaseRemoteConfig();
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("REMOTE_CONFIG", "SUCCESS");
                AppConfig.setFromRemoteConfig(firebaseRemoteConfig);
                startActivityMainDelay(true);
            } else {
                Log.d("REMOTE_CONFIG", "FAILED");
                AppConfig.setFromSharedPreference();
                startActivityMainDelay(true);
            }
        });
    }

    private void startActivityMainDelay(boolean fast) {
        new Handler(this.getMainLooper()).postDelayed(() -> nextActivity(), fast ? 1000 : 2000);
    }

    private void nextActivity() {
        Intent i = new Intent(ActivitySplash.this, ActivityMain.class);
        startActivity(i);
        finish(); // kill current activity
    }


    @Override
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            for (String perm : permissions) {
                boolean rationale = shouldShowRequestPermissionRationale(perm);
                sharedPref.setNeverAskAgain(perm, !rationale);
            }
            requestRemoteConfig();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
