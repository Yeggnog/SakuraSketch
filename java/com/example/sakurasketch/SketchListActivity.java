package com.example.sakurasketch;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class SketchListActivity extends SingleFragmentActivity {

    //private static final String TAG = "SketchListActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = new SketchListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
        //Log.d(TAG, "---- WE GOT HERE ----");
    }

    protected Fragment createFragment() {
        //Log.d(TAG, "---- Created SketchListFragment ----");
        return new SketchListFragment();
    }
}
