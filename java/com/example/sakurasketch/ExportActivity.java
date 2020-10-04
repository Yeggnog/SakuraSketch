package com.example.sakurasketch;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ExportActivity extends SingleFragmentActivity {
    private static Fragment fragment;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = { Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new ExportFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    public static Intent goToExport(Context packageContext, Sketch sk){
        Intent intent = new Intent(packageContext, ExportActivity.class);
        putSketch(sk);
        return intent;
    }

    public static void putSketch(Sketch sk){
        if(fragment != null){
            ((ExportFragment)fragment).getSketch(sk);
        }
    }

    protected Fragment createFragment() {
        return new ExportFragment();
    }

    public void verifyStoragePermissions() {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }
}
